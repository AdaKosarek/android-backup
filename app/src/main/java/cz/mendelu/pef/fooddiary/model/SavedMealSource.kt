package cz.mendelu.pef.fooddiary.model

enum class SavedMealSource {
    API_ONLY, //uložené jen z API (detail → save)
    FAB, //vytvořené ručně přes FAB
    API_PLUS_FAB //API recept + uživatelská data
}
