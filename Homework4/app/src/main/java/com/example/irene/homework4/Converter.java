package com.example.irene.homework4;

import android.content.Context;

public class Converter {
    /* Переводим dp в px */
    public static int dpToPx(Context context, int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
}