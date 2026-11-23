package com.example.chefmateapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_detail)

        val nameTextView = findViewById<TextView>(R.id.recipe_name)
        val imageView = findViewById<ImageView>(R.id.recipe_image)
        val stepsTextView = findViewById<TextView>(R.id.recipe_steps)

        val id = intent.getLongExtra("recipe_id", -1)
        val store = RecipeStore.get(this)
        val recipe = store.find(id = id).firstOrNull()
        if (recipe == null) {
            nameTextView.text = "Recipe unavailable"
            imageView.visibility = android.view.View.GONE
            return
        }
        nameTextView.text = "${recipe.name} • ${recipe.cookTime}"
        if (recipe.imageRes != 0) imageView.setImageResource(recipe.imageRes)
        else imageView.visibility = android.view.View.GONE
        val ingredients = store.ingredients(id)
        findViewById<TextView>(R.id.recipe_ingredients).text =
            if (ingredients.isEmpty()) "No ingredients listed."
            else "Sample ingredients\n" + ingredients.joinToString("\n")
        stepsTextView.text = store.steps(id).joinToString("\n\n")
    }
}
