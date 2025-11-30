package cz.mendelu.pef.fooddiary.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_meals")
data class SavedMeal(

    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0L,

    //pokud mame navic recept
    val apiId: Long? = null,

    val customName: String? = null,
    val userPhotoUri: String? = null,
    val userNote: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,

    val isFavorite: Boolean = false,
    val savedTimestamp: Long
)
