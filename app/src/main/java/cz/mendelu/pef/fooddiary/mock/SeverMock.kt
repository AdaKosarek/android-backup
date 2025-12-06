package cz.mendelu.pef.fooddiary.mock

import cz.mendelu.pef.fooddiary.model.DiscoverRecipeItem
import cz.mendelu.pef.fooddiary.model.Ingredient
import cz.mendelu.pef.fooddiary.model.InstructionBlock
import cz.mendelu.pef.fooddiary.model.InstructionStep
import cz.mendelu.pef.fooddiary.model.Nutrient
import cz.mendelu.pef.fooddiary.model.Nutrition
import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.model.RecipesListResponse

object ServerMock {

    //mock list items
    val recipe1 = DiscoverRecipeItem(
        id = 101,
        title = "Zucchini Lasagna",
        image = "https://spoonacular.com/recipeImages/715538-556x370.jpg",
        imageType = "jpg",
        readyInMinutes = 35,
        dishTypes = listOf("dinner", "main course", "lunch"),
        servings = 4
    )

    val recipe2 = DiscoverRecipeItem(
        id = 102,
        title = "Creamy Tomato Soup",
        image = "https://spoonacular.com/recipeImages/715495-556x370.jpg",
        imageType = "jpg",
        readyInMinutes = 20,
        dishTypes = listOf("soup", "lunch"),
        servings = 2
    )

    val recipe3 = DiscoverRecipeItem(
        id = 103,
        title = "Vegan Pancakes",
        image = "https://spoonacular.com/recipeImages/716429-556x370.jpg",
        imageType = "jpg",
        readyInMinutes = 15,
        dishTypes = listOf("breakfast", "dessert"),
        servings = 3
    )

    val allRecipes = RecipesListResponse(
        results = listOf(recipe1, recipe2, recipe3)
    )

    //mock ingredients
    private val ingredients = listOf(
        Ingredient(
            id = 1,
            name = "Zucchini",
            original = "2 medium zucchinis",
            amount = 2.0,
            unit = "",
            image = "zucchini.jpg"
        ),
        Ingredient(
            id = 2,
            name = "Mozzarella",
            original = "200 g mozzarella",
            amount = 200.0,
            unit = "g",
            image = "mozzarella.png"
        ),
        Ingredient(
            id = 3,
            name = "Tomato Sauce",
            original = "1 cup tomato sauce",
            amount = 1.0,
            unit = "cup",
            image = "tomato-sauce.png"
        )
    )

    //mock nutrition
    private val nutrition = Nutrition(
        nutrients = listOf(
            Nutrient("Calories", 320.0, "kcal", 16.0),
            Nutrient("Protein", 15.0, "g", 30.0),
            Nutrient("Carbohydrates", 25.0, "g", 8.0),
            Nutrient("Fat", 18.0, "g", 22.0)
        )
    )

    //mock instructions
    private val instructionBlocks = listOf(
        InstructionBlock(
            name = "Main",
            steps = listOf(
                InstructionStep(1, "Slice the zucchinis lengthwise."),
                InstructionStep(2, "Layer zucchini, sauce, and cheese in a baking dish."),
                InstructionStep(3, "Bake for 25 minutes until golden.")
            )
        )
    )

    //mock detail
    val recipeDetail = RecipeDetail(
        id = 101,
        title = "Zucchini Lasagna",
        image = recipe1.image,
        readyInMinutes = 35,
        servings = 4,
        dishTypes = listOf("lunch", "main meal"),
        nutrition = nutrition,
        extendedIngredients = ingredients,
        instructions = "Slice zucchinis, layer with sauce and cheese, bake.",
        analyzedInstructions = instructionBlocks
    )
}