package cz.mendelu.pef.fooddiary.model

data class ApiRecipe(
    val id: Long,
    val title: String?,
    val image: String?,
    val servings: Int?,
    val readyInMinutes: Int?,
    val cuisines: List<String>?,
    val dishTypes: List<String>?,
    val summary: String?,
    val instructions: String?,
    val analyzedInstructions: List<InstructionBlock>?,
    val extendedIngredients: List<ApiIngredient>?,
    val nutrition: ApiNutrition?
)