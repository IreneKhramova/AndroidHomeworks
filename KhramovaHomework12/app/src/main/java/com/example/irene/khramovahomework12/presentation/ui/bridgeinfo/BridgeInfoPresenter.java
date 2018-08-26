package com.example.irene.khramovahomework12.presentation.ui.bridgeinfo;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.data.remote.ApiService;
import com.example.irene.khramovahomework12.data.remote.ImageLoader;
import com.example.irene.khramovahomework12.presentation.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BridgeInfoPresenter extends BasePresenter<BridgeInfoMvpView> {

    @NonNull
    private final ApiService apiService;

    @Nullable
    private Disposable disposable;

    public BridgeInfoPresenter(@NonNull ApiService apiService) {
        this.apiService = apiService;
    }

    public void onCreate(int bridgeId) {
        checkViewAttached();
        getBridge(bridgeId);
    }

    public void getBridge(int id) {
        checkViewAttached();
        getMvpView().showProgressView();
        getMvpView().showProgressViewPhoto();
        disposable = apiService.getBridgeInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bridge -> {
                            getMvpView().showBridgeInfo(bridge);
                            getBridgePhoto(bridge);
                        },
                        error -> {
                            error.printStackTrace();
                            getMvpView().showLoadingError();
                            getMvpView().hideProgressViewPhoto();
                        });
    }

    //TODO: как правильно сделать?
    public void getBridgePhoto(Bridge bridge) {
        checkViewAttached();
        ImageView imageView = getMvpView().getPhotoImageView();

        ImageLoader.loadBridgePhoto(imageView.getContext(), bridge, imageView, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                e.printStackTrace();
                Log.d("Load photo", e.getMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                getMvpView().showBridgePhoto(resource);
                Log.d("Load photo", "Done");
                return false;
            }
        });
    }

    @Override
    protected void doUnsubscribe() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
