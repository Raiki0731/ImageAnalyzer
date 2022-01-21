package com.example.imageanalyzer;

import android.util.Log;

import com.google.gson.Gson;

public class JsonParser {

    public ModelForGson parse(String json) {
        Gson gson = new Gson();
        ModelForGson result = gson.fromJson(json, ModelForGson.class);

        Log.v("parse", "OK");
        return result;
    }
}
