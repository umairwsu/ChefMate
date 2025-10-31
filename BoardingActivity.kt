package com.example.chefmateapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chefmateapp.R

class BoardingActivity : AppCompatActivity() {

    private lateinit var nextImageView: ImageView
    private lateinit var startBtn : Button
    private lateinit var titleTextView : TextView
    private lateinit var disTextView : TextView
    private lateinit var boardingImageView: ImageView

    private var flag = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_boarding)

        nextImageView = findViewById(R.id.nextBtn)
        startBtn = findViewById(R.id.stBtn)
        titleTextView = findViewById(R.id.titleTv)
        disTextView = findViewById(R.id.disTv)
        boardingImageView = findViewById(R.id.boarding_iv)


        nextImageView.setOnClickListener {
            when (flag) {
                1 -> {
                    flag = 2
                    boardingImageView.setImageResource(R.drawable.br2)
                    titleTextView.text = "Get the exact nutrition value of everything you eat"
                    disTextView.text = "Cooking can be tough with planning, dietary needs, and ingredients."
                }
                2 -> {
                    nextImageView.visibility = View.GONE
                    startBtn.visibility = View.VISIBLE
                    boardingImageView.setImageResource(R.drawable.br3)
                    titleTextView.text = "Favorite your go to recipes and view them for later"
                    disTextView.text = "Finding suitable recipes and saving them for easier viewing later"
                }
            }


        }
        startBtn.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
        }
    }
}