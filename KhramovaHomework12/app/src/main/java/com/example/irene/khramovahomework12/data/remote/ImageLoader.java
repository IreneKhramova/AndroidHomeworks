package com.example.irene.khramovahomework12.data.remote;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.data.util.DivorceUtil;

public class ImageLoader {

    private static final String BASE_URL = "http://gdemost.handh.ru/";

    public static void loadBridgePhoto(Context context, Bridge bridge, ImageView imageView, RequestListener<Drawable> listener) {
        StringBuilder photoUrl = new StringBuilder(BASE_URL);

        if (DivorceUtil.isDivorced(bridge)) {
            photoUrl.append(bridge.getPhotoClose());
        } else {
            photoUrl.append(bridge.getPhotoOpen());
        }

        Glide.with(context)
                .load(photoUrl.toString())
                .listener(listener)
                .into(imageView);
    }
}
