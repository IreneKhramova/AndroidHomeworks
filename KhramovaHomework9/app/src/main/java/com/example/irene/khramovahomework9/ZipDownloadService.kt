package com.example.irene.khramovahomework9

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.*
import java.util.zip.ZipInputStream


class ZipDownloadService : Service() {
    companion object {
        private const val EXTRA_URL = "ZIP url"
        private const val TAG_DOWNLOAD = "ZipDownload"
        private const val ZIP_BASE_URL = "https://drive.google.com/"
        private const val CHANNEL_ID = "Channel id"
        private const val NOTIFICATION_ID = 42
        private const val MAX_PROGRESS = 100

        fun createStartIntent(context: Context, url: String): Intent {
            val intent = Intent(context, ZipDownloadService::class.java)
            intent.putExtra(EXTRA_URL, url)
            return intent
        }
    }

    private lateinit var zipFileName: String
    private lateinit var zipFileLocation: String
    private lateinit var zipUrl: String
    private lateinit var zipDownloadRetrofit: ZipDownloadRetrofitInterface
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private var disposable: Disposable? = null
    var intentBroadcast = Intent(MainActivity.BROADCAST_ACTION)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_download)
                .setContentTitle(getString(R.string.title_zip))
                .setContentText(getString(R.string.text_downloading))

        zipFileName = filesDir.path + "/zip/myZip.zip"
        zipFileLocation = filesDir.path + "/zip/"

        zipUrl = intent?.getStringExtra(EXTRA_URL).toString()

        val listener = object : DownloadProgressListener {
            override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                val progress = ((bytesRead * MAX_PROGRESS) / contentLength).toInt()
                try {
                    intentBroadcast.putExtra(MainActivity.PARAM_PROGRESS, progress)
                    intentBroadcast.putExtra(MainActivity.PARAM_TASK, MainActivity.TASK_DOWNLOAD)
                    sendBroadcast(intentBroadcast)
                    if (progress != MAX_PROGRESS) {
                        notificationBuilder.setProgress(MAX_PROGRESS, progress, false)
                        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                    } else {
                        notificationBuilder.setContentText(getString(R.string.text_download_success))
                                .setProgress(0, 0, false)
                        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        val interceptor = DownloadProgressInterceptor(listener)

        val retrofit = Retrofit.Builder()
                .baseUrl(ZIP_BASE_URL)
                .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        zipDownloadRetrofit = retrofit.create(ZipDownloadRetrofitInterface::class.java)

        disposable = downloadZip()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG_DOWNLOAD, "zip destroy")

        disposable?.dispose()
        super.onDestroy()
    }

    private fun downloadZip(): Disposable {
        return zipDownloadRetrofit.downloadZip(zipUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Response<ResponseBody>>() {

                    override fun onNext(response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.d(TAG_DOWNLOAD, "server contacted and has file")
                            val responseBody = response.body()
                            if (responseBody != null) {
                                Single.fromCallable {
                                    saveToDisk(responseBody)
                                }
                                        .subscribeOn(Schedulers.io())
                                        .subscribe({
                                            Single.fromCallable {
                                                unzip(zipFileName, zipFileLocation)
                                            }
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe({ name ->
                                                        intentBroadcast.putExtra(MainActivity.PARAM_IMG, name)
                                                        intentBroadcast.putExtra(MainActivity.PARAM_TASK, MainActivity.TASK_UNZIP)
                                                        sendBroadcast(intentBroadcast)
                                                        stopSelf()
                                                    }, { e ->
                                                        Log.d(TAG_DOWNLOAD, e.message)
                                                    })
                                        }, { e ->
                                            Log.d(TAG_DOWNLOAD, e.message)
                                        })
                            } else {
                                Log.d(TAG_DOWNLOAD, "responseBody = null")
                            }
                        } else {
                            Log.d(TAG_DOWNLOAD, "server contact failed")
                        }
                    }

                    override fun onComplete() {
                        Log.d(TAG_DOWNLOAD, "server onComplete")
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG_DOWNLOAD, e.localizedMessage)
                    }
                })
    }

    private fun saveToDisk(body: ResponseBody) {
        try {
            notificationBuilder.setContentText(getString(R.string.text_saving_zip))
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

            File(zipFileLocation).mkdir()
            val destinationFile = File(zipFileName)

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                Log.d(TAG_DOWNLOAD, "File Size=" + body.contentLength())

                inputStream = body.byteStream()
                outputStream = FileOutputStream(destinationFile)

                val data = ByteArray(4096)
                var count: Int
                var progress = 0
                count = inputStream.read(data)
                while (count != -1) {
                    progress += count
                    if (body.contentLength() > 0) {
                        Log.d(TAG_DOWNLOAD, "Progress: " + (progress * MAX_PROGRESS / body.contentLength()))
                        if (progress != MAX_PROGRESS) {
                            notificationBuilder.setProgress(MAX_PROGRESS, progress, false)
                            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                        } else {
                            notificationBuilder.setContentText(getString(R.string.text_download_success))
                                    .setProgress(0, 0, false)
                            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
                        }
                    }
                    outputStream.write(data, 0, count)
                    count = inputStream.read(data)
                }

                Log.d(TAG_DOWNLOAD, "File saved successfully!")
                return
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG_DOWNLOAD, "Failed to save the file!")
                return
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG_DOWNLOAD, "Failed to save the file!")
            return
        }
    }

    fun unzip(zipFile: String, targetLocation: String): String? {
        notificationBuilder.setContentText(getString(R.string.text_unpack_zip))
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        var name: String? = null
        var zin: ZipInputStream? = null

        try {
            val fin = FileInputStream(zipFile)
            zin = ZipInputStream(fin)

            val zipEntry = zin.nextEntry
            name = targetLocation + zipEntry.name
            val fout = FileOutputStream(name)

            var c = zin.read()
            while (c != -1) {
                fout.write(c)
                c = zin.read()
            }

            zin.closeEntry()
            fout.close()
        } catch (e: Exception) {
            println(e)
        } finally {
            zin?.close()
        }
        return name
    }
}