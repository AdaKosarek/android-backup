package cz.mendelu.pef.fooddiary.model

data class Ingredient(
    val id: Long?,
    val name: String?,
    val original: String?,
    val amount: Double?,
    val unit: String?,
    val image: String?
)
