package cz.mendelu.pef.fooddiary.model

data class ApiIngredient(
    val id: Long?,
    val name: String?,
    val original: String?,
    val originalName: String?,
    val amount: Double?,
    val unit: String?,
    val image: String?
)