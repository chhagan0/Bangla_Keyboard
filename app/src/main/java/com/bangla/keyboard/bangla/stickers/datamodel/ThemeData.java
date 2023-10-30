package com.bangla.keyboard.bangla.stickers.datamodel;

public class ThemeData {
    private int resourceId;
    private String text;

    public ThemeData(int resourceId, String text) {
        this.resourceId = resourceId;
        this.text = text;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getText() {
        return text;
    }
}

