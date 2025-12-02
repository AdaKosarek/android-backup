package cz.mendelu.pef.fooddiary.model

data class Nutrient(
    val name: String?,
    val amount: Double?,
    val unit: String?,
    val percentOfDailyNeeds: Double?
)