package com.example.irene.khramovahomework12.domain.provider;

import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.data.model.BridgeResponse;
import com.example.irene.khramovahomework12.data.remote.ApiService;

import java.util.List;
import io.reactivex.Single;

public class BridgesProvider {

    private final ApiService apiService;

    public BridgesProvider(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<List<Bridge>> getBridges() {
        return apiService.getBridges()
                .map(BridgeResponse::getBridges);
    }

    public Single<Bridge> getBridgeInfo(int id) {
        return apiService.getBridgeInfo(id);
    }
}
