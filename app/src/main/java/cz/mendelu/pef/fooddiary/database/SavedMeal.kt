package cz.mendelu.pef.fooddiary.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.pef.fooddiary.model.Ingredient
import cz.mendelu.pef.fooddiary.model.InstructionBlock
import cz.mendelu.pef.fooddiary.model.Nutrition
import cz.mendelu.pef.fooddiary.model.SavedMealSource

@Entity(tableName = "saved_meals")
data class SavedMeal(
    val source: SavedMealSource,

    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0L,

    //pokud mame navic recept
    val apiId: Long? = null,
    val title: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val dishTypes: List<String>?,

    val nutrition: Nutrition?,
    val extendedIngredients: List<Ingredient>?,
    val instructions: String?,
    val analyzedInstructions: List<InstructionBlock>?,

    //pokud ukladame jidlo pres FAB
    val customName: String? = null,
    val userPhotoUri: String? = null,
    val userNote: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val placeName: String? = null,

    val isFavorite: Boolean = false,
    val savedTimestamp: Long
){
    val hasLocation: Boolean
        get() = latitude != null && longitude != null
}
