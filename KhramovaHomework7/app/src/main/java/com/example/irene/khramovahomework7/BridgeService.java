package com.example.irene.khramovahomework7;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface BridgeService {
    @GET("/api/v1/bridges/?format=json")
    Single<Response> getData();
}
