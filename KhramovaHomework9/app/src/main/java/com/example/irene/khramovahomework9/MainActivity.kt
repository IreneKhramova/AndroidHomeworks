package com.example.irene.khramovahomework9

import android.content.ComponentName
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.widget.Button
import android.widget.TextView
import android.app.PendingIntent
import android.content.Intent



class MainActivity : AppCompatActivity() {
    companion object {
        private const val ZIP_URL = "https://my-files.ru/Save/g8i167/firefox.zip/";
        //"https://drive.google.com/open?id=1fPAla77UWq7ITrI4gHtHCCD8I9qfQiTZ/"
        //https://my-files.ru/Save/g8i167/firefox.zip
    }

    private var weatherService: WeatherService? = null
    private var isBound = false
    private val textView: TextView by bindView(R.id.textView)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraintLayout)
    private val buttonDownload: Button by bindView(R.id.buttonDownload)

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

        buttonDownload.setOnClickListener {
            startService(ZipDownloadService.createStartIntent(this, ZIP_URL))
            /*val notification = Notification(R.drawable.icon, getText(R.string.ticker_text),
                    System.currentTimeMillis())
            val notificationIntent = Intent(this, ExampleActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
            notification.setLatestEventInfo(this, getText(R.string.notification_title),
                    getText(R.string.notification_message), pendingIntent)
            startForeground(ONGOING_NOTIFICATION_ID, notification)*/
        }
    }

    override fun onDestroy() {
        if(isBound) {
            unbindService(serviceConnection)
            isBound = false
        }
        super.onDestroy()
    }

    fun onWeatherLoadSuccess(temperature: String) {
        textView.text = temperature
    }

    fun onWeatherLoadError() {
        Snackbar
                .make(constraintLayout, "При загрузке данных произошла ошибка", Snackbar.LENGTH_INDEFINITE)
                //.setAction("Повторить", { view -> weatherService?.disposable = weatherService?.loadWeather() })
                .show()
    }
}
