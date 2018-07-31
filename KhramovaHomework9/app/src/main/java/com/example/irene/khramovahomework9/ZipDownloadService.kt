package com.example.irene.khramovahomework9

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File.separator
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import okio.Okio
import okio.BufferedSink
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe


class ZipDownloadService : Service() {
    companion object {
        private const val EXTRA_URL = "ZIP url"
        private const val TAG = "ZipDownload"

        fun createStartIntent(context: Context, url: String) : Intent {
            val intent = Intent(context, ZipDownloadService::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }

    private lateinit var zipUrl: String
    private lateinit var zipDownloadRetrofit: ZipDownloadRetrofitInterface
    var disposable: Disposable? = null
    var onLoadSuccess: ((String) -> (Unit))? = null
    var onLoadError: (() -> (Unit))? = null

    override fun onCreate() {
        super.onCreate()

        /*val gson = GsonBuilder()
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://yadi.sk")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        zipDownloadRetrofit = retrofit.create(ZipDownloadRetrofitInterface::class.java)

        disposable = downloadZip()*/
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        zipUrl = intent?.getStringExtra(EXTRA_URL).toString()

        val retrofit = Retrofit.Builder()
                .baseUrl(zipUrl)
                .client(OkHttpClient.Builder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        zipDownloadRetrofit = retrofit.create(ZipDownloadRetrofitInterface::class.java)

        disposable = downloadZip()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("destroy", "zip destroy")

        disposable?.dispose()
        super.onDestroy()
    }

    private fun downloadZip(): Disposable {
        return zipDownloadRetrofit.downloadZip("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


                /*.doOnError { e ->
                    //onLoadError?.invoke()
                    Log.d("Load", e.localizedMessage)
                }*/
                /*.flatMap { response ->
                    Observable.create(ObservableOnSubscribe<File> { emitter ->
                        try {
                            // you can access headers of response
                            val header = response.headers().get("Content-Disposition")
                            // this is specific case, it's up to you how you want to save your file
                            // if you are not downloading file from direct link, you might be lucky to obtain file name from header
                            //val fileName = header?.replace("attachment; filename=", "")
                            // will create file in global Music directory, can be any other directory, just don't forget to handle permissions
                            //val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absoluteFile, fileName)
                            val file = File(filesDir, "myZip"*//*fileName*//*)
                            Log.d("Zip written", "write")

                            val sink = Okio.buffer(Okio.sink(file))
                            // you can access body of response
                            sink.writeAll(response.body()!!.source())
                            sink.close()
                            emitter.onNext(file)
                            emitter.onComplete()
                        } catch (e: IOException) {
                            Log.d("Zip err", e.message.toString())

                            e.printStackTrace()
                            emitter.onError(e)

                        }
                    })
                }
                .subscribeWith(object: DisposableObserver<File>() {
                    override fun onComplete() {
                        Log.d(TAG, "server onComplete")
                        stopSelf()
                    }

                    override fun onNext(t: File) {
                        Log.d(TAG, "server contacted and has file")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Zip load", e.message.toString())
                    }*/
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
                                            Log.d(TAG, "saved")
                                        }, { e ->
                                    Log.d(TAG, e.message)
                                })
                            } else {
                                Log.d(TAG, "bad(((((")
                            }
                            //val writtenToDisk = writeResponseBodyToDisk(response.body())
                           // Log.d(TAG, "file download was a success? $writtenToDisk")
                            //stopSelf()
                        } else {
                            Log.d(TAG, "server contact failed")
                        }                    }

                    override fun onComplete() {
                        Log.d(TAG, "server onComplete")

                        stopSelf()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Zip load", e.localizedMessage)
                    }

                })
    }

    private fun saveToDisk(body: ResponseBody) {
    try {
        File(getFilesDir().getPath() + "zip").mkdir();
        val destinationFile = File(getFilesDir().getPath() + "zip/myZip");

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
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
        }
    } catch (e: IOException) {
        e.printStackTrace();
        Log.d(TAG, "Failed to save the file!");
        return;
    }
}

    private fun writeResponseBodyToDisk(body: ResponseBody?): Boolean {
        try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile = File(filesDir.toString() + separator + "imageZip")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body?.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body?.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream!!.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream!!.flush()

                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            return false
        }

    }
}