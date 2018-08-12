package com.example.irene.khramovahomework7;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BridgeAdapter.OnItemClick, MapFragment.OnBridgeInfoClick {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.linearLayout) LinearLayout mLinearLayout;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    public static final String BASE_URL = "http://gdemost.handh.ru/";
    private static final String DATE_FORMAT = "H:mm";
    private static final int STATE_MAP = 1;
    private static final int STATE_MAIN = 2;
    private Disposable mDisposable;
    private BridgeService mBridgeService;
    private static int mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.inflateMenu(R.menu.menu_map);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.to_map:
                    onItemToMapClick();
                    return true;

                case R.id.to_main:
                    onItemToMainClick();
                    return true;
            }
            return false;
        });

        Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mBridgeService = retrofit.create(BridgeService.class);

        onItemToMainClick();
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(Bridge bridge) {
        startInfoActivity(bridge);
    }

    @Override
    public void onInfoClick(Bridge bridge) {
        startInfoActivity(bridge);
    }

    private void startInfoActivity(Bridge bridge) {
        MainActivity.this.startActivity(InfoActivity.createStartIntent(MainActivity.this, bridge));
    }

    private Disposable load() {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        return mBridgeService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                        if(mState == STATE_MAIN) {
                            ((BridgesListFragment) getSupportFragmentManager()
                                    .findFragmentByTag(BridgesListFragment.TAG_LIST))
                                    .setBridges(response.getObjects());
                        } else {
                            ((MapFragment) getSupportFragmentManager()
                                    .findFragmentByTag(MapFragment.TAG_MAP))
                                    .setBridges(response.getObjects());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        Snackbar
                                .make(mLinearLayout, R.string.error_load_text, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.error_load_retry, view -> mDisposable = load())
                                .show();
                        Log.d("Loading",e.getMessage());
                    }
                });
    }

    private void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, fragment, tag).commit();
    }

    private void onItemToMapClick() {
        mState = STATE_MAP;
        mToolbar.getMenu().findItem(R.id.to_map).setVisible(false);
        mToolbar.getMenu().findItem(R.id.to_main).setVisible(true);
        Log.d("Menu", "to map");

        changeFragment(MapFragment.newInstance(), MapFragment.TAG_MAP);

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = load();
    }

    private void onItemToMainClick() {
        mState = STATE_MAIN;
        mToolbar.getMenu().findItem(R.id.to_map).setVisible(true);
        mToolbar.getMenu().findItem(R.id.to_main).setVisible(false);
        Log.d("Menu", "to main");

        changeFragment(BridgesListFragment.newInstance(), BridgesListFragment.TAG_LIST);

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = load();
    }
}
