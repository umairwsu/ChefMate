package com.example.chefmateapp

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import com.example.ChefMate.model.Recipe
import org.json.JSONArray

class RecipeStore private constructor(private val context: Context) :
    SQLiteOpenHelper(context, "chefmate.db", null, 1) {

    override fun onConfigure(db: SQLiteDatabase) = db.setForeignKeyConstraintsEnabled(true)

    override fun onCreate(db: SQLiteDatabase) {
        val schema = context.assets.open("schema.sql").bufferedReader().use { it.readText() }
        schema.split(';').map { it.trim() }.filter { it.isNotEmpty() }.forEach { db.execSQL(it) }
        db.execSQL("INSERT INTO Category VALUES (10,'Dinner'),(20,'Uncategorized')")
        val recipes = JSONArray(context.assets.open("recipes.json").bufferedReader().use { it.readText() })
        for (index in 0 until recipes.length()) {
            val row = recipes.getJSONObject(index)
            val id = row.getLong("recipe_id")
            val category = if (id == 1L || id == 9L) 10 else 20
            db.execSQL("INSERT INTO Recipe VALUES (?,?,?,?,?)", arrayOf<Any>(
                id, row.getString("name"), row.getString("image_name"), row.getInt("cook_minutes"), category
            ))
            val steps = row.getJSONArray("steps")
            for (stepIndex in 0 until steps.length()) {
                val step = steps.getJSONObject(stepIndex)
                db.execSQL("INSERT INTO RecipeStep VALUES (?,?,?)", arrayOf<Any>(
                    id, step.getInt("step_number"), step.getString("instruction")
                ))
            }
        }
        db.execSQL("INSERT INTO Ingredient VALUES (101,'Salmon'),(102,'Salt'),(103,'Chicken')")
        db.execSQL("INSERT INTO RecipeIngredient VALUES (1,101,2,'pieces'),(1,102,0.5,'tsp'),(9,102,1,'tsp'),(9,103,500,'g')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        error("No migration from version $oldVersion to $newVersion")
    }

    fun find(term: String = "", maxMinutes: Int? = null, id: Long? = null): List<Recipe> {
        val conditions = mutableListOf("instr(lower(name), lower(?)) > 0")
        val args = mutableListOf(term.trim())
        // SQLite instr(text, '') returns 1, so an empty term shows every recipe.
        if (maxMinutes != null) {
            conditions.add("cook_minutes <= CAST(? AS INTEGER)")
            args.add(maxMinutes.toString())
        }
        if (id != null) {
            conditions.add("recipe_id = ?")
            args.add(id.toString())
        }
        return readableDatabase.rawQuery(
            "SELECT recipe_id,name,image_name,cook_minutes FROM Recipe WHERE " +
                conditions.joinToString(" AND ") + " ORDER BY recipe_id",
            args.toTypedArray()
        ).use { rows ->
            buildList {
                while (rows.moveToNext()) {
                    val drawable = context.resources.getIdentifier(rows.getString(2), "drawable", context.packageName)
                    add(Recipe(rows.getString(1), drawable, "${rows.getInt(3)} minutes", "", rows.getLong(0)))
                }
            }
        }
    }

    fun steps(id: Long): List<String> = readableDatabase.rawQuery(
        "SELECT step_number,instruction FROM RecipeStep WHERE recipe_id = ? ORDER BY step_number",
        arrayOf(id.toString())
    ).use { rows ->
        buildList { while (rows.moveToNext()) add("${rows.getInt(0)}. ${rows.getString(1)}") }
    }

    fun ingredients(id: Long): List<String> = readableDatabase.rawQuery(
        """SELECT i.ingredient_name, printf('%g', ri.quantity), ri.unit
           FROM RecipeIngredient ri JOIN Ingredient i ON i.ingredient_id = ri.ingredient_id
           WHERE ri.recipe_id = ? ORDER BY i.ingredient_id""",
        arrayOf(id.toString())
    ).use { rows ->
        buildList { while (rows.moveToNext()) add("${rows.getString(0)}: ${rows.getString(1)} ${rows.getString(2)}") }
    }

    fun add(name: String, minutes: Int, steps: List<String>) {
        require(name.isNotBlank() && minutes > 0 && steps.isNotEmpty() && steps.none { it.isBlank() })
        val db = writableDatabase
        db.beginTransaction()
        try {
            val id = db.compileStatement("INSERT INTO Recipe(name,image_name,cook_minutes,category_id) VALUES (?,'',?,20)").use {
                it.bindString(1, name.trim())
                it.bindLong(2, minutes.toLong())
                it.executeInsert()
            }
            steps.forEachIndexed { index, step ->
                db.execSQL("INSERT INTO RecipeStep VALUES (?,?,?)", arrayOf<Any>(id, index + 1, step))
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    companion object {
        private var instance: RecipeStore? = null
        @Synchronized fun get(context: Context): RecipeStore =
            instance ?: RecipeStore(context.applicationContext).also { instance = it }
    }
}

internal fun AppCompatActivity.openRecipe(id: Long) =
    startActivity(Intent(this, RecipeDetailActivity::class.java).putExtra("recipe_id", id))
