
package com.example.imageanalyzer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Parent implements Serializable {

    @SerializedName("object")
    private String object;
    @SerializedName("confidence")
    private Double confidence;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

}
