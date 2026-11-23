package com.example.chefmateapp

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ChefMate.adapter.RecipeAdapter

class Home : AppCompatActivity() {

    private lateinit var titleTextView:TextView
    private lateinit var searchBtn : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)


        titleTextView = findViewById<TextView>(R.id.ch_tv)
        searchBtn = findViewById<ImageView>(R.id.search_img)
        inIt()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }



    }
    override fun onResume() {
        super.onResume()
        findViewById<RecyclerView>(R.id.recyclerView).adapter =
            RecipeAdapter(RecipeStore.get(this).find()) { openRecipe(it.id) }
    }

    private fun inIt(){
        val text = "Chef Mate"
        val spannable = SpannableString(text)

        spannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#FF6901")), 5, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        titleTextView.text = spannable
    }

}
