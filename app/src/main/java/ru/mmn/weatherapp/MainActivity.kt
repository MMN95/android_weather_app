package ru.mmn.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button:Button = findViewById(R.id.button)
        val textView:TextView = findViewById(R.id.text_view)

        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                textView.text = ekbWeather.toString()
            }
        })
    }
}