"""Run from any directory with Python 3 to verify ChefMate's SQLite data."""
import json
import sqlite3
from pathlib import Path

assets = Path(__file__).resolve().parents[1] / "app/src/main/assets"
db = sqlite3.connect(":memory:")
db.execute("PRAGMA foreign_keys = ON")
db.executescript((assets / "schema.sql").read_text())
db.executemany("INSERT INTO Category VALUES (?,?)", [(10, "Dinner"), (20, "Uncategorized")])
for row in json.loads((assets / "recipes.json").read_text()):
    recipe_id = row["recipe_id"]
    db.execute("INSERT INTO Recipe VALUES (?,?,?,?,?)", (
        recipe_id, row["name"], row["image_name"], row["cook_minutes"],
        10 if recipe_id in (1, 9) else 20
    ))
    db.executemany("INSERT INTO RecipeStep VALUES (?,?,?)", [
        (recipe_id, step["step_number"], step["instruction"]) for step in row["steps"]
    ])
db.executescript("""
INSERT INTO Ingredient VALUES (101,'Salmon'),(102,'Salt'),(103,'Chicken');
INSERT INTO RecipeIngredient VALUES (1,101,2,'pieces'),(1,102,0.5,'tsp'),(9,102,1,'tsp'),(9,103,500,'g');
""")
assert db.execute("SELECT COUNT(*) FROM Recipe").fetchone() == (14,)
assert db.execute("SELECT COUNT(*) FROM RecipeStep").fetchone() == (78,)
assert db.execute("SELECT step_number FROM RecipeStep WHERE recipe_id=? ORDER BY step_number", (9,)).fetchall() == [(i,) for i in range(1, 7)]
assert db.execute("SELECT ingredient_name, quantity FROM RecipeIngredient JOIN Ingredient USING(ingredient_id) WHERE recipe_id=9 ORDER BY ingredient_id").fetchall() == [("Salt", 1), ("Chicken", 500)]
search = "SELECT name FROM Recipe WHERE instr(lower(name),lower(?))>0"
assert db.execute(search, ("butter chicken",)).fetchall() == [("Butter Chicken",)]
assert db.execute(search, ("zzznomatch",)).fetchall() == []
ids = {row[0] for row in db.execute("SELECT recipe_id FROM Recipe WHERE cook_minutes<=CAST(? AS INTEGER)", ("30",))}
assert {1, 5} <= ids and not {2, 9} & ids
bug = db.execute("SELECT name FROM Recipe WHERE printf('%d minutes',cook_minutes) <= '30 minutes'").fetchall()
assert ("Traditional Irish Stew",) in bug
assert db.execute("PRAGMA foreign_key_check").fetchall() == []
for statement in (
    "INSERT INTO RecipeStep VALUES (999,1,'Orphan')",
    "INSERT INTO RecipeStep VALUES (1,1,'Duplicate')",
    "INSERT INTO Recipe(name,image_name,cook_minutes,category_id) VALUES ('Invalid','',0,20)",
):
    try:
        db.execute(statement)
    except sqlite3.IntegrityError:
        continue
    raise AssertionError(f"Unexpectedly accepted: {statement}")
print("Passed: import, ordered details, ingredients, search, time filter, and integrity constraints.")
print("Confirmed: the text comparison wrongly includes 110 minutes. The integer filter excludes it.")
