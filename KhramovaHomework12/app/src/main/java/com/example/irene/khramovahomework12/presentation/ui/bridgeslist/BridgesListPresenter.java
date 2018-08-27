package com.example.irene.khramovahomework12.presentation.ui.bridgeslist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.irene.khramovahomework12.domain.provider.BridgesProvider;
import com.example.irene.khramovahomework12.presentation.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BridgesListPresenter extends BasePresenter<BridgesListMvpView> {

    @NonNull
    private final BridgesProvider bridgesProvider;

    @Nullable
    private Disposable disposable;

    public BridgesListPresenter(@NonNull BridgesProvider bridgesProvider) {
        this.bridgesProvider = bridgesProvider;
    }

    public void onCreate() {
        checkViewAttached();
        getBridges();
    }

    public void getBridges() {
        checkViewAttached();
        getMvpView().showProgressView();
        disposable = bridgesProvider.getBridges()
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
