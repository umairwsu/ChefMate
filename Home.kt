package com.example.chefmateapp

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ChefMate.adapter.RecipeAdapter
import com.example.ChefMate.model.Recipe
import com.example.chefmateapp.R

class Home : AppCompatActivity() {

    private lateinit var titleTextView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)


        titleTextView = findViewById<TextView>(R.id.ch_tv)
        inIt()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = RecipeAdapter(recipes)

    }
    private fun inIt(){
        val text = "Chef Mate"
        val spannable = SpannableString(text)

        spannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#FF6901")), 5, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        titleTextView.text = spannable
    }

    private val recipes = listOf(
        Recipe(
            name = "Fried Salmon Steak",
            imageRes = R.drawable.r1,
            cookTime = "20 minutes",
            steps = """
            1. Pat dry the salmon steaks with paper towels.
            2. Rub with salt, black pepper, and lemon juice.
            3. Heat olive oil in a pan on medium heat.
            4. Place salmon steaks and cook for 4–5 minutes on each side until golden brown.
            5. Garnish with fresh herbs and serve hot with vegetables or salad.
        """.trimIndent()
        ),

        Recipe(
            name = "Traditional Irish Stew",
            imageRes = R.drawable.r2,
            cookTime = "110 minutes",
            steps = """
            1. Cut lamb or beef into chunks and season with salt and pepper.
            2. In a large pot, heat oil and sear the meat until browned.
            3. Add chopped onions, carrots, and potatoes.
            4. Pour in beef stock or water, cover, and simmer for 1 hour.
            5. Stir occasionally until meat and vegetables are tender.
            6. Garnish with parsley before serving.
        """.trimIndent()
        ),

        Recipe(
            name = "Turkish Eggs with Bread",
            imageRes = R.drawable.r3,
            cookTime = "15 minutes",
            steps = """
            1. Heat olive oil in a pan, add garlic and red chili flakes.
            2. Break eggs into the pan and cook until whites are set but yolks are runny.
            3. Sprinkle salt, pepper, and fresh herbs.
            4. Toast bread slices and place on a plate.
            5. Serve hot eggs over bread with yogurt or cheese on the side.
        """.trimIndent()
        ),

        Recipe(
            name = "Club Sandwich",
            imageRes = R.drawable.r4,
            cookTime = "20 minutes",
            steps = """
            1. Toast three slices of bread until golden.
            2. Spread mayonnaise on each slice.
            3. Layer with lettuce, tomato slices, cooked chicken, and fried egg.
            4. Place cheese and crispy bacon (optional).
            5. Stack the bread slices, secure with toothpicks, and cut into triangles.
        """.trimIndent()
        ),

        Recipe(
            name = "Classic Margherita Pizza",
            imageRes = R.drawable.r5,
            cookTime = "30 minutes",
            steps = """
            1. Roll out pizza dough into a thin base.
            2. Spread tomato sauce evenly over the base.
            3. Add fresh mozzarella slices and basil leaves.
            4. Drizzle olive oil on top.
            5. Bake in a preheated oven at 220°C (425°F) for 10–12 minutes until cheese melts and crust is golden.
        """.trimIndent()
        ),

        Recipe(
            name = "Vegetable Stir Fry",
            imageRes = R.drawable.r6,
            cookTime = "15 minutes",
            steps = """
            1. Heat oil in a wok or large pan.
            2. Add garlic and ginger, stir-fry for 30 seconds.
            3. Add chopped vegetables (carrot, bell pepper, broccoli, beans).
            4. Pour soy sauce and a little vinegar.
            5. Stir-fry for 5–6 minutes until veggies are slightly tender but still crisp.
            6. Serve with rice or noodles.
        """.trimIndent()
        ),

        Recipe(
            name = "Spaghetti Aglio e Olio",
            imageRes = R.drawable.r7,
            cookTime = "20 minutes",
            steps = """
            1. Boil spaghetti with salt until al dente, then drain.
            2. In a pan, heat olive oil and add sliced garlic.
            3. Add red chili flakes and cook until garlic turns golden.
            4. Toss boiled spaghetti into the oil mixture.
            5. Season with salt and chopped parsley.
            6. Serve with grated parmesan cheese.
        """.trimIndent()
        ),

        Recipe(
            name = "Chicken Alfredo Pasta",
            imageRes = R.drawable.r8,
            cookTime = "30 minutes",
            steps = """
            1. Boil pasta until cooked, then drain.
            2. In a pan, cook chicken strips with salt, pepper, and garlic.
            3. Add butter and cream, stir well.
            4. Mix in parmesan cheese until sauce thickens.
            5. Toss pasta in the sauce and cook for 2–3 minutes.
            6. Garnish with parsley and serve hot.
        """.trimIndent()
        ),

        Recipe(
            name = "Butter Chicken",
            imageRes = R.drawable.r9,
            cookTime = "50 minutes",
            steps = """
            1. Marinate chicken with yogurt, ginger–garlic paste, and spices for 30 minutes.
            2. Grill or pan-fry chicken until lightly cooked.
            3. In a pan, cook butter with onions, garlic, and tomatoes.
            4. Add cream, cashew paste, and spices to make a thick gravy.
            5. Add chicken pieces and simmer for 15 minutes.
            6. Garnish with cream and serve with naan or rice.
        """.trimIndent()
        ),

        Recipe(
            name = "Homemade Pancakes",
            imageRes = R.drawable.r10,
            cookTime = "20 minutes",
            steps = """
            1. Mix flour, sugar, baking powder, and salt in a bowl.
            2. Add milk, egg, and melted butter; whisk into a smooth batter.
            3. Heat a pan and grease lightly.
            4. Pour batter in small rounds, cook until bubbles form, then flip.
            5. Cook the other side until golden.
            6. Serve with honey, maple syrup, or fruits.
        """.trimIndent()
        ),

        Recipe(
            name = "Chocolate Brownies",
            imageRes = R.drawable.r11,
            cookTime = "40 minutes",
            steps = """
            1. Melt butter and chocolate together.
            2. Add sugar, eggs, and vanilla extract; whisk well.
            3. Mix in flour and cocoa powder.
            4. Pour batter into a greased baking tray.
            5. Bake at 180°C (350°F) for 25–30 minutes.
            6. Cool, cut into squares, and serve.
        """.trimIndent()
        ),

        Recipe(
            name = "Grilled Lemon Herb Chicken",
            imageRes = R.drawable.r12,
            cookTime = "35 minutes",
            steps = """
            1. Marinate chicken breasts with lemon juice, olive oil, garlic, and herbs.
            2. Preheat grill or pan.
            3. Grill chicken for 6–8 minutes per side until fully cooked.
            4. Brush with leftover marinade while cooking.
            5. Serve with salad or grilled vegetables.
        """.trimIndent()
        ),

        Recipe(
            name = "Chicken Caesar Salad",
            imageRes = R.drawable.r13,
            cookTime = "20 minutes",
            steps = """
            1. Grill chicken breast and slice into strips.
            2. Toss chopped romaine lettuce with Caesar dressing.
            3. Add croutons and grated parmesan cheese.
            4. Place chicken slices on top.
            5. Garnish with more dressing and black pepper.
        """.trimIndent()
        ),

        Recipe(
            name = "Creamy Tomato Basil Soup",
            imageRes = R.drawable.r14,
            cookTime = "30 minutes",
            steps = """
            1. Heat butter in a pot and sauté onions and garlic.
            2. Add chopped tomatoes and cook until soft.
            3. Blend the mixture into a smooth puree.
            4. Return to the pot, add cream, salt, and pepper.
            5. Simmer for 10 minutes.
            6. Garnish with fresh basil and serve hot.
        """.trimIndent()
        )
    )
}