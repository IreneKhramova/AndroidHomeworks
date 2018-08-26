package com.example.irene.khramovahomework12.data.remote;

import android.content.Context;

import com.example.irene.khramovahomework12.data.model.Bridge;
import com.example.irene.khramovahomework12.data.model.BridgeResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    String TIME_FORMAT = "H:mm";
    String ENDPOINT = "http://gdemost.handh.ru/api/v1/";

    /********
     * Helper class that sets up a new services
     *******/
    class Creator {
        public static ApiService newApiService(Context context) {

            Gson gson = new GsonBuilder()
                    .setDateFormat(TIME_FORMAT)
                    .create();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

            okBuilder.addInterceptor(new ChuckInterceptor(context));


            OkHttpClient client = okBuilder.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(ApiService.class);
        }
    }

    @GET("bridges/?format=json")
    Single<BridgeResponse> getBridges();

    @GET("bridges/{id}/?format=json")
    Single<Bridge> getBridgeInfo(@Path("id") int bridgeId);
}
