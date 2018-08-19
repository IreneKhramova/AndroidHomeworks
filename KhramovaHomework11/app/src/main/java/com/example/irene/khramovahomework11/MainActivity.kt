package com.example.irene.khramovahomework11

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val columnsView = findViewById<ColumnsView>(R.id.columnsView)

        //TODO: даты и значения
        val data = LinkedHashMap<String, Int>()
        data.put("04.05", 46)
        data.put("05.05", 30)
        data.put("30.05", 60)
        data.put("01.06", 30)
        data.put("02.06", 35)
        data.put("03.06", 50)
        data.put("04.06", 46)
        data.put("05.06", 30)
        data.put("06.06", 30)

        columnsView.setData(data)
    }
}
