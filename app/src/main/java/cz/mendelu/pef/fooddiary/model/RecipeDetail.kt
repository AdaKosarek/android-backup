package cz.mendelu.pef.fooddiary.model

data class RecipeDetail(
    val id: Long,
    val title: String?,
    val image: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val dishTypes: List<String>?,
    val nutrition: Nutrition?,
    val extendedIngredients: List<Ingredient>?,
    val instructions: String?,
    val analyzedInstructions: List<InstructionBlock>?
)