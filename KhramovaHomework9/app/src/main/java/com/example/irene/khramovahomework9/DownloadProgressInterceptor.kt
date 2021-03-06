package com.example.irene.khramovahomework9

import okhttp3.Interceptor
import okhttp3.Response

class DownloadProgressInterceptor(var listener: DownloadProgressListener?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        return originalResponse
                .newBuilder()
                .body(DownloadProgressResponseBody(originalResponse.body()!!, listener))
                .build()
    }

}