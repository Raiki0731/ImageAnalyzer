package com.example.imageanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class DrawingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ModelForGson result = (ModelForGson) getIntent().getSerializableExtra("result");
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), (Uri) intent.getExtras().get("uri"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Drawing drawing = new Drawing(this, bitmap, result);
        setContentView(drawing);
    }
}
