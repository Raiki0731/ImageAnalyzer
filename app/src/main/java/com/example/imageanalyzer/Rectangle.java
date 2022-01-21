
package com.example.imageanalyzer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rectangle implements Serializable {

    @SerializedName("x")
    private Integer x;
    @SerializedName("y")
    private Integer y;
    @SerializedName("w")
    private Integer w;
    @SerializedName("h")
    private Integer h;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

}
