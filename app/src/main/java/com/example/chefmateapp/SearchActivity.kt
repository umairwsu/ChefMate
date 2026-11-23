package com.example.chefmateapp

import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ChefMate.adapter.RecipeAdapter

class SearchActivity : AppCompatActivity() {
    private val store by lazy { RecipeStore.get(this) }
    private lateinit var term: EditText
    private lateinit var limit: EditText
    private lateinit var status: TextView
    private lateinit var results: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        term = findViewById(R.id.search_et)
        limit = findViewById(R.id.max_minutes)
        status = findViewById(R.id.search_status)
        results = findViewById(R.id.recyclerView_search)
        results.layoutManager = LinearLayoutManager(this)
        term.doAfterTextChanged { refresh() }
        limit.doAfterTextChanged { refresh() }
        findViewById<Button>(R.id.add_recipe).setOnClickListener { addRecipe() }
        refresh()
    }

    private fun refresh() {
        val raw = limit.text.toString().trim()
        val minutes = raw.toIntOrNull()
        if (raw.isNotEmpty() && (minutes == null || minutes <= 0)) {
            limit.error = "Enter a positive whole number"
            status.text = "Correct the time filter to see recipes."
            results.visibility = View.GONE
            return
        }
        limit.error = null
        val recipes = store.find(term.text.toString(), minutes)
        results.visibility = View.VISIBLE
        status.text = if (recipes.isEmpty()) "No recipes found." else "${recipes.size} recipes"
        results.adapter = RecipeAdapter(recipes) { openRecipe(it.id) }
    }

    private fun addRecipe() {
        val fields = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val name = EditText(this).apply { hint = "Recipe name" }
        val time = EditText(this).apply { hint = "Cooking minutes"; inputType = InputType.TYPE_CLASS_NUMBER }
        val instructions = EditText(this).apply {
            hint = "One instruction per line"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }
        fields.addView(name)
        fields.addView(time)
        fields.addView(instructions)
        val dialog = AlertDialog.Builder(this).setTitle("Add recipe").setView(fields)
            .setNegativeButton("Cancel", null).setPositiveButton("Save", null).create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val minutes = time.text.toString().trim().toIntOrNull()
                val steps = instructions.text.toString().lines().map { it.trim() }.filter { it.isNotEmpty() }
                when {
                    name.text.isBlank() -> name.error = "Enter a recipe name"
                    minutes == null || minutes <= 0 -> time.error = "Enter positive cooking minutes"
                    steps.isEmpty() -> instructions.error = "Enter at least one instruction"
                    else -> {
                        try {
                            store.add(name.text.toString(), minutes, steps)
                            dialog.dismiss()
                            refresh()
                        } catch (e: SQLiteException) {
                            name.error = "Could not save the recipe. Try again."
                        }
                    }
                }
            }
        }
        dialog.show()
    }
}
