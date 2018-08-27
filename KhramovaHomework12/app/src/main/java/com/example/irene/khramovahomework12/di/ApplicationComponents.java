package com.example.irene.khramovahomework12.di;

import android.content.Context;

import com.example.irene.khramovahomework12.data.remote.ApiService;
import com.example.irene.khramovahomework12.domain.provider.BridgesProvider;
import com.example.irene.khramovahomework12.presentation.ui.bridgeinfo.BridgeInfoPresenter;
import com.example.irene.khramovahomework12.presentation.ui.bridgeslist.BridgesAdapter;
import com.example.irene.khramovahomework12.presentation.ui.bridgeslist.BridgesListPresenter;

public class ApplicationComponents {

    private static volatile ApplicationComponents instance;

    private final ApiService apiService;
    private final BridgesProvider bridgesProvider;
    private Context context;

    private ApplicationComponents(Context context) {
        this.context = context;
        this.apiService = ApiService.Creator.newApiService(context);
        this.bridgesProvider = new BridgesProvider(apiService);
    }

    public static ApplicationComponents getInstance(Context context) {
        ApplicationComponents localInstance = instance;
        if (localInstance == null) {
            synchronized (ApplicationComponents.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApplicationComponents(context);
                }
            }
        }
        return localInstance;
    }

    public ApiService provideApiService() {
        return apiService;
    }

    public Context provideContext() {
        return context;
    }

    public BridgesAdapter provideBridgesAdapter(BridgesAdapter.OnItemClick onClick) {
        return new BridgesAdapter(onClick);
    }

    public BridgesListPresenter provideBridgesListPresenter() {
        return new BridgesListPresenter(provideBridgesProvider());
    }

    public BridgeInfoPresenter provideBridgeInfoPresenter() {
        return new BridgeInfoPresenter(provideBridgesProvider());
    }

    public BridgesProvider provideBridgesProvider() {
        return bridgesProvider;
    }
}
