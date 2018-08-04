package com.example.irene.khramovahomework9

import android.content.*
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    companion object {
        private const val ZIP_URL = "https://drive.google.com/uc?export=download&id=1fPAla77UWq7ITrI4gHtHCCD8I9qfQiTZ"
        const val PARAM_PROGRESS = "Progress"
        const val PARAM_TASK = "Task"
        const val TASK_DOWNLOAD = 1
        const val TASK_UNZIP = 2
        const val PARAM_IMG = "Path to image"
        const val BROADCAST_ACTION = "com.example.irene.khramovahomework9"
    }

    private var weatherService: WeatherService? = null
    private var isBound = false
    private val textViewTemperature: TextView by bindView(R.id.textViewTemperature)
    private val progressBar: ProgressBar by bindView(R.id.progressBar)
    private val imageView: ImageView by bindView(R.id.imageView)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraintLayout)
    private val buttonDownload: Button by bindView(R.id.buttonDownload)
    private lateinit var broadcastReceiver: BroadcastReceiver

    private var serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as WeatherService.WeatherBinder
            weatherService = binder.getService()
            weatherService?.onLoadSuccess = ::onWeatherLoadSuccess
            weatherService?.onLoadError = ::onWeatherLoadError
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent = WeatherService.createStartIntent(this)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val task = intent.getIntExtra(PARAM_TASK, 0)
                when (task) {
                    TASK_DOWNLOAD -> {
                        val progress = intent.getIntExtra(PARAM_PROGRESS, 0)
                        progressBar.progress = progress
                    }
                    TASK_UNZIP -> {
                        val imgPath = intent.getStringExtra(PARAM_IMG)
                        imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath))
                    }
                }
            }
        }

        val intentFilter = IntentFilter(BROADCAST_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)

        buttonDownload.setOnClickListener {
            startService(ZipDownloadService.createStartIntent(this, ZIP_URL))
        }
    }

    override fun onDestroy() {
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    fun onWeatherLoadSuccess(temperature: String) {
        textViewTemperature.text = temperature
    }

    fun onWeatherLoadError() {
        Snackbar
                .make(constraintLayout, "При загрузке данных произошла ошибка", Snackbar.LENGTH_INDEFINITE)
                //.setAction("Повторить", { view -> weatherService?.disposable = weatherService?.loadWeather() })
                .show()
    }
}
