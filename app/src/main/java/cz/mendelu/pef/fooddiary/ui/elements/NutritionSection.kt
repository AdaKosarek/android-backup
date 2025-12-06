package cz.mendelu.pef.fooddiary.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.ui.theme.BlueLight
import cz.mendelu.pef.fooddiary.ui.theme.GreenLight
import cz.mendelu.pef.fooddiary.ui.theme.OrangeLight
import cz.mendelu.pef.fooddiary.ui.theme.PinkLight
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin

@Composable
fun NutritionSection(recipe: RecipeDetail) {
    val nutrients = recipe.nutrition?.nutrients ?: return

    val calories = nutrients.find { it.name.equals("Calories", true) }
    val protein = nutrients.find { it.name.equals("Protein", true) }
    val carbs   = nutrients.find { it.name.equals("Carbohydrates", true) }
    val fat     = nutrients.find { it.name.equals("Fat", true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = basicMargin()),
        horizontalArrangement = Arrangement.spacedBy(halfMargin())
    ) {

        calories?.let { NutrientBox("${it.amount?.toInt()} kcal", "calories", OrangeLight) }
        protein?.let { NutrientBox("${it.amount?.toInt()} g", "protein", BlueLight) }
        carbs?.let   { NutrientBox("${it.amount?.toInt()} g", "carbohydrates", GreenLight) }
        fat?.let     { NutrientBox("${it.amount?.toInt()} g", "fat", PinkLight) }
    }
}