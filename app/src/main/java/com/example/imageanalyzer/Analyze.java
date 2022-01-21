package com.example.imageanalyzer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class Analyze extends AsyncTask {
    @SuppressLint("StaticFieldLeak")
    private final Activity mActivity;

    public Analyze(Activity activity) {

        mActivity = activity;
    }

    private Uri uri;
    private AnalyzingView analyzingView;

    @Override
    protected void onPreExecute() {
        analyzingView = new AnalyzingView(mActivity);
        analyzingView.show();
    }

    @Override
    protected String doInBackground(java.lang.Object[] objects) {
        HttpsURLConnection connection = null;
        StringBuilder stringBuilder = new StringBuilder();
        String json;
        uri = (Uri)objects[3];
        InputStream inputStream = null;
        Bitmap bitmap = (Bitmap) objects[2];

        try {
            Matrix matrix = new Matrix();
            if (bitmap.getWidth() > bitmap.getHeight()) {
                matrix.postRotate(90);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);


            URL url = new URL(objects[0] + "vision/v3.2/detect?model-version=latest");
            connection = (HttpsURLConnection)url.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(30000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", (String)objects[1]);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            OutputStream output = new BufferedOutputStream(connection.getOutputStream());
            output.write(byteArrayOutputStream.toByteArray());
            output.flush();
            connection.connect();


            /*
            int responseCode;
            responseCode = connection.getResponseCode();
            String responseMessage;
            responseMessage = connection.getResponseMessage();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while(true){
                String data = br.readLine();
                if(data == null){
                    break;
                }
                System.out.println(data);
            }
            System.out.println("responseCode:" + responseCode + " responseMSG:" + responseMessage);
            System.out.print(connection.getHeaderFields());
            */


            inputStream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null)
            connection.disconnect();
        }
        json = stringBuilder.toString();
        System.out.println(json);
        return json;
    }

    @Override
    protected void onPostExecute(java.lang.Object object) {

        JsonParser jsonParser = new JsonParser();
        ModelForGson result = jsonParser.parse(object.toString());

        if (result.getObjects().size() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
            alertDialog.setMessage("画像の中に読み取れるオブジェクトがありませんでした。");
            alertDialog.setPositiveButton("OK", null);
            analyzingView.dismiss();
            alertDialog.show();

        } else {
            Intent intent = new Intent(mActivity, DrawingActivity.class);
            intent.putExtra("result", result);
            intent.putExtra("uri", uri);
            analyzingView.dismiss();

            mActivity.startActivity(intent);

        }

    }

}