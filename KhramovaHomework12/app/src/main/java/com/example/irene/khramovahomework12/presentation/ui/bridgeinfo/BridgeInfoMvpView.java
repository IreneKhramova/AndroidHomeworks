package com.example.irene.khramovahomework12.presentation.ui.bridgeinfo;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.presentation.ui.base.MvpView;

public interface BridgeInfoMvpView extends MvpView {
    void showLoadingError();

    void showBridgeInfo(Bridge bridge);

    void showProgressView();

    void showProgressViewPhoto();

    void hideProgressViewPhoto();

    void showBridgePhoto(Drawable resource);

    ImageView getPhotoImageView();
}
