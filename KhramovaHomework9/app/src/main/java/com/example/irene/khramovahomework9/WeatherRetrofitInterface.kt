package com.example.irene.khramovahomework9

import com.example.irene.khramovahomework9.data.Response
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface WeatherRetrofitInterface {
    @GET("/data/2.5/weather?q=saransk&units=metric&appid=a924f0f5b30839d1ecb95f0a6416a0c2")
    fun requestWeather(): Observable<Response>
}