package com.example.irene.khramovahomework9

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import io.reactivex.Observable


class ZipDownloadService : Service() {
    companion object {
        private const val EXTRA_URL = "ZIP url"
        private const val TAG = "ZipDownload"
        private const val ZIP_BASE_URL = "https://drive.google.com/"

        fun createStartIntent(context: Context, url: String) : Intent {
            val intent = Intent(context, ZipDownloadService::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }

    private lateinit var zipUrl: String
    private lateinit var zipDownloadRetrofit: ZipDownloadRetrofitInterface
    var disposable: Disposable? = null
    //var onLoadSuccess: ((String) -> (Unit))? = null
    //var onLoadError: (() -> (Unit))? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        zipUrl = intent?.getStringExtra(EXTRA_URL).toString()

        val retrofit = Retrofit.Builder()
                .baseUrl(ZIP_BASE_URL)
                .client(OkHttpClient.Builder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        zipDownloadRetrofit = retrofit.create(ZipDownloadRetrofitInterface::class.java)

        disposable = downloadZip()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "zip destroy")

        disposable?.dispose()
        super.onDestroy()
    }

    private fun downloadZip(): Disposable {
        return zipDownloadRetrofit.downloadZip(zipUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<Response<ResponseBody>>() {
                    override fun onNext(response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "server contacted and has file")
                            var rb = response.body()
                            if (rb != null) {
                                Observable.fromCallable{
                                    saveToDisk(rb)
                                }
                                        .subscribeOn(Schedulers.io())
                                        .subscribe ({
                                            //Log.d(TAG, "saved")
                                        }, { e ->
                                    Log.d(TAG, e.message)
                                })
                            } else {
                                Log.d(TAG, "bad(((((")
                            }
                            //stopSelf()
                        } else {
                            Log.d(TAG, "server contact failed")
                        }
                    }

                    override fun onComplete() {
                        Log.d(TAG, "server onComplete")

                        stopSelf()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Zip load err", e.localizedMessage)
                    }

                })
    }

    private fun saveToDisk(body: ResponseBody) {
    try {
        File(filesDir.getPath() + "zip").mkdir();
        val destinationFile = File(filesDir.path + "zip/myZip");

        var inputStream: InputStream? = null;
        var outputStream: OutputStream? = null;

        try {
            Log.d(TAG, "File Size=" + body.contentLength());

            inputStream = body.byteStream();
            outputStream = FileOutputStream(destinationFile);

            var data: ByteArray = byteArrayOf()//Array(4096, {i -> (0 as Byte)});
            var count: Int;
            var progress = 0;
            count = inputStream.read(data)
            while (count != -1) {
                outputStream.write(data, 0, count);
                progress +=count;
                Log.d(TAG, "Progress: " + progress + "/" + body.contentLength() + " >>>> " + progress*1.0/body.contentLength());
                count = inputStream.read(data)
            }

            outputStream.flush();

            Log.d(TAG, "File saved successfully!");
            return;
        } catch (e: IOException) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        } finally {
            inputStream?.close();
            outputStream?.close();
        }
        } catch (e: IOException) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }
}