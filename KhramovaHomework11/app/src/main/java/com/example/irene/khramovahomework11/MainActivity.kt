package com.example.irene.khramovahomework11

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val MILLIS_IN_DAY = 1000 * 60 * 60 * 24
        const val COLUMNS_COUNT = 9
    }

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val columnsView = findViewById<ColumnsView>(R.id.columnsView)
        val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
        val data = LinkedHashMap<String, Int>()

        for (i in COLUMNS_COUNT-1 downTo 0) {
            val index = dateFormat.format(Date(System.currentTimeMillis() - i * MILLIS_IN_DAY))
            data[index] = getRandomValue()
        }

        columnsView.setData(data)
    }

    private fun getRandomValue(): Int {
        return random.nextInt(100) + 1
    }
}
