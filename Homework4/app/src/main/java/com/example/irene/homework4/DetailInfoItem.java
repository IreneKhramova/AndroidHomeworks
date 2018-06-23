package com.example.irene.homework4;

public class DetailInfoItem extends BaseInfoItem {
    private String textInfo;
    private boolean needAttention;

    DetailInfoItem(String title, int icon, String textInfo, boolean needAttention) {
        super(title, icon);
        this.textInfo = textInfo;
        this.needAttention = needAttention;
    }

    public String getTextInfo() {
        return textInfo;
    }

    public boolean isNeedAttention() {
        return needAttention;
    }
}
