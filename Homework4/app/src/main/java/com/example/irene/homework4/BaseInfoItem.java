package com.example.irene.homework4;

public class BaseInfoItem {
    private String title;
    private int icon;

    public BaseInfoItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }
}
