package com.example.imageanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v -> openCamera());
    }

    private static final int REQUEST_CAPTURE = 625;
    private Uri mUri;
    private File tempFile;
    private void openCamera() {
        if (!CheckNetwork.isConnected(this)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("インターネットに接続していません。");
            alertDialog.setPositiveButton("OK", null);
            alertDialog.show();
        } else {
            File captureImage = createOutputFile();
            mUri = FileProvider.getUriForFile(this, "com.example.imageanalyzer.fileprovider", captureImage);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAPTURE);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUri);
                Analyze analyze = new Analyze(this);
                analyze.execute(Auth.getEndPoint(), Auth.getApiKey(), bitmap, mUri);
                tempFile.delete();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private File createOutputFile() {
        File tempFile = new File(this.getFilesDir(), "temp/sample");

        try {
            Objects.requireNonNull(tempFile.getParentFile()).mkdirs();
            tempFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile;
    }

}