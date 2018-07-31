package com.example.irene.khramovahomework9

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ZipDownloadRetrofitInterface {
    @Streaming
    @GET
    fun downloadZip(@Url fileUrl: String): Observable<Response<ResponseBody>>
}