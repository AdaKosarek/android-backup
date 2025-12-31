package cz.mendelu.pef.fooddiary.model

enum class SavedMealSource {
    API_ONLY, //API recept (detail -> save)
    FAB, //uzivatelska data
    API_PLUS_FAB //API recept + uživatelská data
}
