package com.example.irene.khramovahomework9

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.util.Log
import com.example.irene.khramovahomework9.data.Response
import io.reactivex.observers.DisposableObserver
import java.util.concurrent.TimeUnit


class WeatherService : Service() {
    companion object {
        fun createStartIntent(context: Context) : Intent {
            return Intent(context, WeatherService::class.java)
        }

        private const val BASE_URL = "http://api.openweathermap.org/"
    }

    private val weatherBinder = WeatherBinder()
    var onLoadSuccess: ((String) -> (Unit))? = null
    var onLoadError: (() -> (Unit))? = null
    private lateinit var weatherRetrofit: WeatherRetrofitInterface
    var disposable: Disposable? = null

    override fun onBind(p0: Intent?): IBinder {
        return weatherBinder
    }

    override fun onCreate() {
        super.onCreate()

        val gson = GsonBuilder()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        weatherRetrofit = retrofit.create(WeatherRetrofitInterface::class.java)

        disposable = loadWeather()
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    private fun loadWeather() : Disposable {
        return weatherRetrofit.requestWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen { observable ->
                    observable.delay(60, TimeUnit.SECONDS);
                }
                .subscribeWith(object: DisposableObserver<Response>() {
                    override fun onComplete() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onNext(response: Response) {
                        onLoadSuccess?.invoke(response.main?.temp.toString())
                        Log.d("Load", "success")
                    }

                    override fun onError(e: Throwable) {
                        onLoadError?.invoke()
                        Log.d("Load", e.message)
                    }
                })
    }

    inner class WeatherBinder : Binder() {
        fun getService() : WeatherService {
            return this@WeatherService
        }
    }
}