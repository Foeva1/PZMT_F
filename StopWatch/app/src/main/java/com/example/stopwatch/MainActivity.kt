package com.example.stopwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    lateinit var stopwatch:Chronometer
    var running      = false
    var offset: Long = 0

    val OFFSET_KEY   = "offset"
    val RUNNING_KEY  = "running"
    val BASE_KEY     = "base"

    //Схороняем переменнын
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY,offset)
        savedInstanceState.putBoolean(RUNNING_KEY,running)
        savedInstanceState.putLong(BASE_KEY,stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        if (running){
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if(running){
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }


    //Обновляем stopwatch.base
    fun setBaseTime(){
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    //Схороняем offset
    fun saveOffset(){
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stopwatch       = findViewById<Chronometer>(R.id.stopwatch)

        //Загрузка состояний
        if (savedInstanceState != null){
            offset  = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()}
            else
                setBaseTime()
        }

        //----Кнопки
        //Запускаем секундомер
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener{
            if(!running){
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }

        //Останавливаем секундомер
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener{
            if(running){
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }

        //Зануляем offset и stopwatch.base
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener{
            offset = 0
            setBaseTime()
        }
    }
}