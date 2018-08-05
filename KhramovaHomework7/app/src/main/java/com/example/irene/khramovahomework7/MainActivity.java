package com.example.irene.khramovahomework7;

import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

public class MainActivity extends AppCompatActivity implements BridgeAdapter.OnItemClick {
    public static final String DATE_FORMAT = "H:mm";
    public static final String BASE_URL = "http://gdemost.handh.ru/";
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.linearLayout) LinearLayout mLinearLayout;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    private Disposable mDisposable;
    BridgeService mBridgeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayoutContainer, BridgesListFragment.newInstance(), BridgesListFragment.TAG_LIST)
                .commit();

        Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mBridgeService = retrofit.create(BridgeService.class);

        mDisposable = load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.to_map).setChecked(true);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        super.onDestroy();
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

                        ((BridgesListFragment) getSupportFragmentManager()
                                .findFragmentByTag(BridgesListFragment.TAG_LIST))
                                .setBridges(response.getObjects());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        Snackbar
                                .make(mLinearLayout, R.string.error_load_text, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.error_load_retry, view -> {
                                    mDisposable = load();
                                })
                                .show();
                        //Log.d("Loading",e.getMessage());
                    }
                });
    }

    @Override
    public void onClick(Bridge bridge) {
        MainActivity.this.startActivity(InfoActivity.createStartIntent(MainActivity.this, bridge));
    }
}
