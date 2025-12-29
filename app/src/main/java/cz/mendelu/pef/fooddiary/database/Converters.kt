package cz.mendelu.pef.fooddiary.database

import androidx.room.TypeConverter
import cz.mendelu.pef.fooddiary.model.Ingredient
import cz.mendelu.pef.fooddiary.model.InstructionBlock
import cz.mendelu.pef.fooddiary.model.Nutrition
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //ENUM
    @TypeConverter
    fun fromSource(source: SavedMealSource): String = source.name

    @TypeConverter
    fun toSource(value: String): SavedMealSource =
        SavedMealSource.valueOf(value)

    //List<String>
    @TypeConverter
    fun fromDishTypes(list: List<String>?): String? {
        if (list == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).toJson(list)
    }

    @TypeConverter
    fun toDishTypes(json: String?): List<String>? {
        if (json == null) return null
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).fromJson(json)
    }

    //Nutrition
    @TypeConverter
    fun fromNutrition(nutrition: Nutrition?): String? {
        if (nutrition == null) return null
        return moshi.adapter(Nutrition::class.java).toJson(nutrition)
    }

    @TypeConverter
    fun toNutrition(json: String?): Nutrition? {
        if (json == null) return null
        return moshi.adapter(Nutrition::class.java).fromJson(json)
    }

    //Ingredients
    @TypeConverter
    fun fromIngredients(list: List<Ingredient>?): String? {
        if (list == null) return null
        val type = Types.newParameterizedType(List::class.java, Ingredient::class.java)
        return moshi.adapter<List<Ingredient>>(type).toJson(list)
    }

    @TypeConverter
    fun toIngredients(json: String?): List<Ingredient>? {
        if (json == null) return null
        val type = Types.newParameterizedType(List::class.java, Ingredient::class.java)
        return moshi.adapter<List<Ingredient>>(type).fromJson(json)
    }

    //InstructionBlock
    @TypeConverter
    fun fromInstructionBlocks(list: List<InstructionBlock>?): String? {
        if (list == null) return null
        val type = Types.newParameterizedType(List::class.java, InstructionBlock::class.java)
        return moshi.adapter<List<InstructionBlock>>(type).toJson(list)
    }

    @TypeConverter
    fun toInstructionBlocks(json: String?): List<InstructionBlock>? {
        if (json == null) return null
        val type = Types.newParameterizedType(List::class.java, InstructionBlock::class.java)
        return moshi.adapter<List<InstructionBlock>>(type).fromJson(json)
    }
}
