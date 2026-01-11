package cz.mendelu.pef.fooddiary.utils

object MealInputValidator {

    fun isCustomNameValid(customName: String): Boolean {
        return customName.isNotBlank()
    }

    fun normalizePlaceName(placeName: String): String? {
        return placeName.trim().ifBlank { null }
    }

    fun canSaveMeal(customName: String): Boolean {
        return isCustomNameValid(customName)
    }
}
