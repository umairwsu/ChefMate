package com.example.chefmateapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, BoardingActivity::class.java))
            finish()
        }, 3000)
    }
}