package com.example.irene.khramovahomework12.presentation.ui.bridgeslist;

import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.presentation.ui.base.MvpView;

import java.util.List;

public interface BridgesListMvpView extends MvpView {

    void showLoadingError();

    void showBridges(List<Bridge> bridges);

    void showProgressView();

    void startInfoActivity(int bridgeId);
}
