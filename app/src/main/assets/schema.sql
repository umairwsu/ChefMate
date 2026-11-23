CREATE TABLE Category(category_id INTEGER PRIMARY KEY, category_name TEXT NOT NULL UNIQUE);
CREATE TABLE Recipe(recipe_id INTEGER PRIMARY KEY, name TEXT NOT NULL CHECK(length(trim(name))>0), image_name TEXT NOT NULL, cook_minutes INTEGER NOT NULL CHECK(typeof(cook_minutes)='integer' AND cook_minutes>0), category_id INTEGER NOT NULL REFERENCES Category(category_id));
CREATE TABLE RecipeStep(recipe_id INTEGER NOT NULL REFERENCES Recipe(recipe_id) ON DELETE CASCADE, step_number INTEGER NOT NULL CHECK(step_number>0), instruction TEXT NOT NULL CHECK(length(trim(instruction))>0), PRIMARY KEY(recipe_id,step_number));
CREATE TABLE Ingredient(ingredient_id INTEGER PRIMARY KEY, ingredient_name TEXT NOT NULL UNIQUE);
CREATE TABLE RecipeIngredient(recipe_id INTEGER NOT NULL REFERENCES Recipe(recipe_id) ON DELETE CASCADE, ingredient_id INTEGER NOT NULL REFERENCES Ingredient(ingredient_id), quantity REAL NOT NULL CHECK(quantity>0), unit TEXT NOT NULL, PRIMARY KEY(recipe_id,ingredient_id));
