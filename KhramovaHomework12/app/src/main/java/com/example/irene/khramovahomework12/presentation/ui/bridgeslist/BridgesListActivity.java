package com.example.irene.khramovahomework12.presentation.ui.bridgeslist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.example.irene.khramovahomework12.R;
import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.presentation.ui.base.BaseActivity;
import com.example.irene.khramovahomework12.presentation.ui.bridgeinfo.BridgeInfoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BridgesListActivity extends BaseActivity implements BridgesListMvpView, BridgesAdapter.OnItemClick {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_DATA = 1;
    private static final int VIEW_ERROR = 2;

    @BindView(R.id.viewFlipper) ViewFlipper viewFlipper;
    @BindView(R.id.buttonRetry) Button buttonRetry;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private BridgesListPresenter bridgesListPresenter;
    private BridgesAdapter bridgesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridges_list);
        ButterKnife.bind(this);

        bridgesListPresenter = getApplicationComponents().provideBridgesListPresenter();
        bridgesAdapter = getApplicationComponents().provideBridgesAdapter(this);

        bridgesListPresenter.attachView(this);

        buttonRetry.setOnClickListener(view -> bridgesListPresenter.getBridges());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bridgesAdapter);

        bridgesListPresenter.onCreate();

    }

    @Override
    protected void onDestroy() {
        bridgesListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showLoadingError() {
        viewFlipper.setDisplayedChild(VIEW_ERROR);
    }

    @Override
    public void showBridges(List<Bridge> bridges) {
        viewFlipper.setDisplayedChild(VIEW_DATA);
        bridgesAdapter.setBridges(bridges);
    }

    @Override
    public void showProgressView() {
        viewFlipper.setDisplayedChild(VIEW_LOADING);
    }

    //TODO: так правильно?
    @Override
    public void startInfoActivity(int bridgeId) {
        startActivity(BridgeInfoActivity.createStartIntent(BridgesListActivity.this, bridgeId));
    }

    @Override
    public void onClick(Bridge bridge) {
        bridgesListPresenter.onBridgeItemClick(bridge.getId());
    }
}
