package com.example.chefmateapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost.Companion.OpenAI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AiRecipeActivity : AppCompatActivity() {

    private lateinit var inputDish: EditText
    private lateinit var generateBtn: Button
    private lateinit var resultText: TextView

    private lateinit var progressBar: ProgressBar


    private val openAI = OpenAI(
        OpenAIConfig(
            token = "REPLACE_WITH_BACKEND"
        )
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ai_recipe)


        inputDish = findViewById(R.id.inputDish)
        generateBtn = findViewById(R.id.generateBtn)
        resultText = findViewById(R.id.resultText)
        progressBar = findViewById(R.id.progressBar)

        generateBtn.setOnClickListener {
            val dishName = inputDish.text.toString().trim()
            if (dishName.isNotEmpty()) {
                generateRecipe(dishName)
            } else {
                Toast.makeText(this, "Please enter a dish name!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateRecipe(dish: String) {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
            generateBtn.isEnabled = false
            inputDish.isEnabled = false
            resultText.text = "Cooking something up for you... 🍳"
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = openAI.chatCompletion(
                    ChatCompletionRequest(
                        model = ModelId("gpt-3.5-turbo"),
                        messages = listOf(
                            ChatMessage(
                                ChatRole.System,
                                "You are a professional cooking assistant AI. Give clear and easy recipes."
                            ),
                            ChatMessage(
                                ChatRole.User,
                                "Give me the recipe for $dish including ingredients, cooking time, and step-by-step method."
                            )
                        )
                    )
                )

                val message = response.choices.firstOrNull()?.message?.content ?: "No recipe found."

                runOnUiThread {
                    resultText.text = message
                    progressBar.visibility = View.GONE
                    generateBtn.isEnabled = true
                    inputDish.isEnabled = true
                    inputDish.text.clear() // 🧹 clear input field
                }

            } catch (e: Exception) {
                Log.e("TAG", "generateRecipe: error: ${e.message}", )
                runOnUiThread {
                    resultText.text = "Error: ${e.message}"
                    progressBar.visibility = View.GONE
                    generateBtn.isEnabled = true
                    inputDish.isEnabled = true
                }
            }
        }
    }
}}