package com.moto.pixelr.model;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 */

public class Pixel {

    private int pixelResource;
    private String pixelDescription;

    public Pixel(int resource, String text) {
        this.pixelResource = resource;
        this.pixelDescription = text;
    }

    public int getPixelResource() {
        return pixelResource;
    }

    public void setPixelResource(int pixelResource) {
        this.pixelResource = pixelResource;
    }

    public String getPixelDescription() {
        return pixelDescription;
    }

    public void setPixelDescription(String pixelDescription) {
        this.pixelDescription = pixelDescription;
    }
}
