package cz.mendelu.pef.fooddiary

import cz.mendelu.pef.fooddiary.utils.MealInputValidator
import org.junit.Assert.*
import org.junit.Test

class MealInputValidatorTest {

    @Test
    fun customName_isValid_whenNotBlank() {
        val input = "Burger"

        val result = MealInputValidator.isCustomNameValid(input)

        assertEquals(true, result)
    }

    @Test
    fun customName_isInvalid_whenBlank() {
        val input = ""

        val result = MealInputValidator.isCustomNameValid(input)

        assertEquals(false, result)
    }

    @Test
    fun placeName_isNormalized_whenNotBlank() {
        val input = " Brno "

        val result = MealInputValidator.normalizePlaceName(input)

        assertEquals("Brno", result)
    }

    @Test
    fun placeName_isNull_whenBlank() {
        val input = "   "

        val result = MealInputValidator.normalizePlaceName(input)

        assertEquals(null, result)
    }

    @Test
    fun meal_canBeSaved_whenCustomNameIsValid() {
        val input = "Dinner"

        val result = MealInputValidator.canSaveMeal(input)

        assertEquals(true, result)
    }

     @Test
    fun meal_cannotBeSaved_whenCustomNameIsInvalid() {
        val input = ""

        val result = MealInputValidator.canSaveMeal(input)

        assertEquals(false, result)
    }
}
