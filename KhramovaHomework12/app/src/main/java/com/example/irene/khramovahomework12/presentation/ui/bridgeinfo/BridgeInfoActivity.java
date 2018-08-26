package com.example.irene.khramovahomework12.presentation.ui.bridgeinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.irene.khramovahomework12.R;
import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.data.util.DivorceUtil;
import com.example.irene.khramovahomework12.presentation.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BridgeInfoActivity extends BaseActivity implements BridgeInfoMvpView {

    public static final String EXTRA_ID = "Bridge id";
    private static final int VIEW_LOADING = 0;
    private static final int VIEW_DATA = 1;
    private static final int VIEW_ERROR = 2;

    @BindView(R.id.viewFlipperInfo) ViewFlipper viewFlipperInfo;
    @BindView(R.id.toolbarImage) Toolbar toolbarImage;
    @BindView(R.id.imageViewPhoto) ImageView imageViewPhoto;
    @BindView(R.id.progressBarPhoto) ProgressBar progressBarPhoto;
    @BindView(R.id.textViewDescription) TextView textViewDescription;
    @BindView(R.id.buttonRetryInfo) Button buttonRetryInfo;
    @BindView(R.id.fragment) View viewFragment;

    private BridgeInfoPresenter bridgeInfoPresenter;
    private ImageView imageViewBridge;
    private TextView textViewTitle;
    private TextView textViewDivorceTime;

    public static Intent createStartIntent(Context context, int bridgeId) {
        Intent intent = new Intent(context, BridgeInfoActivity.class);
        intent.putExtra(EXTRA_ID, bridgeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_info);
        ButterKnife.bind(this);

        bridgeInfoPresenter = getApplicationComponents().provideBridgeInfoPresenter();
        bridgeInfoPresenter.attachView(this);

        setSupportActionBar(toolbarImage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageViewBridge = viewFragment.findViewById(R.id.imageViewBridge);
        textViewTitle = viewFragment.findViewById(R.id.textViewTitle);
        textViewDivorceTime = viewFragment.findViewById(R.id.textViewDivorceTime);

        int bridgeId = getIntent().getExtras().getInt(EXTRA_ID);
        buttonRetryInfo.setOnClickListener(view -> bridgeInfoPresenter.getBridge(bridgeId));

        bridgeInfoPresenter.onCreate(bridgeId);
    }

    @Override
    protected void onDestroy() {
        bridgeInfoPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showBridgeInfo(Bridge bridge) {
        viewFlipperInfo.setDisplayedChild(VIEW_DATA);

        imageViewBridge.setImageResource(DivorceUtil.getDivorceImgResId(bridge));
        textViewTitle.setText(bridge.getName());
        textViewDivorceTime.setText(DivorceUtil.getDivorceTime(bridge));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewDescription.setText(Html.fromHtml(bridge.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            textViewDescription.setText(Html.fromHtml(bridge.getDescription()));
        }
    }

    @Override
    public void showBridgePhoto(Drawable resource) {
        progressBarPhoto.setVisibility(View.GONE);
        imageViewPhoto.setImageDrawable(resource);
    }

    @Override
    public void showProgressView() {
        viewFlipperInfo.setDisplayedChild(VIEW_LOADING);
    }

    @Override
    public void showLoadingError() {
        viewFlipperInfo.setDisplayedChild(VIEW_ERROR);
    }

    @Override
    public void showProgressViewPhoto() {
        progressBarPhoto.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white),
                PorterDuff.Mode.MULTIPLY);
        progressBarPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressViewPhoto() {
        progressBarPhoto.setVisibility(View.GONE);
    }

    @Override
    public ImageView getPhotoImageView() {
        return imageViewPhoto;
    }
}
