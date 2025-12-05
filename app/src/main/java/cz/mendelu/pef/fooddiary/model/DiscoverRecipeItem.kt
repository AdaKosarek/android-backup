package cz.mendelu.pef.fooddiary.model

data class DiscoverRecipeItem(
    val id: Long,
    val title: String?,
    val image: String?,
    val imageType: String?,
    val readyInMinutes: Int?,
    val dishTypes: List<String>?,
    val servings: Int?
)
