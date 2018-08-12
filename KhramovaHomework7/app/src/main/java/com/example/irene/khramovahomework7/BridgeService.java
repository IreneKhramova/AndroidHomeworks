package com.example.irene.khramovahomework7;

import com.example.irene.khramovahomework7.data.Response;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface BridgeService {
    @GET("/api/v1/bridges/?format=json")
    Single<Response> getData();
}
