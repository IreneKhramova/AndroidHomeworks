package com.example.irene.khramovahomework12.presentation.ui.bridgeslist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.irene.khramovahomework12.data.model.BridgeResponse;
import com.example.irene.khramovahomework12.data.remote.ApiService;
import com.example.irene.khramovahomework12.presentation.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BridgesListPresenter extends BasePresenter<BridgesListMvpView> {

    @NonNull
    private final ApiService apiService;

    @Nullable
    private Disposable disposable;

    public BridgesListPresenter(@NonNull ApiService apiService) {
        this.apiService = apiService;
    }

    public void onCreate() {
        checkViewAttached();
        getBridges();
    }

    public void getBridges() {
        checkViewAttached();
        getMvpView().showProgressView();
        disposable = apiService.getBridges()
                .map(BridgeResponse::getBridges)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bridges -> getMvpView().showBridges(bridges),
                        error -> {
                            error.printStackTrace();
                            getMvpView().showLoadingError();
                        });
    }

    public void onBridgeItemClick(int bridgeId) {
        getMvpView().startInfoActivity(bridgeId);
    }

    @Override
    protected void doUnsubscribe() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
