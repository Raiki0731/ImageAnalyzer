package com.example.imageanalyzer;

public class Auth {

    public static String getApiKey() {
        return BuildConfig.apiKey;
    }

    public static String getEndPoint() {
        return BuildConfig.endPoint;
    }
}
