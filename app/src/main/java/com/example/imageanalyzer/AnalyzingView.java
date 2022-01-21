package com.example.imageanalyzer;

import android.app.ProgressDialog;
import android.content.Context;

public class AnalyzingView {
    ProgressDialog progressDialog;

    AnalyzingView(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Analyzer");
        progressDialog.setMessage("Analyzing");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void show() {
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }
}
