package cz.mendelu.pef.fooddiary.ui.elements.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.ui.theme.BlueDark
import cz.mendelu.pef.fooddiary.ui.theme.BlueLight
import cz.mendelu.pef.fooddiary.ui.theme.GreenDark
import cz.mendelu.pef.fooddiary.ui.theme.GreenLight
import cz.mendelu.pef.fooddiary.ui.theme.OrangeDark
import cz.mendelu.pef.fooddiary.ui.theme.OrangeLight
import cz.mendelu.pef.fooddiary.ui.theme.PinkDark
import cz.mendelu.pef.fooddiary.ui.theme.PinkLight
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin

@Composable
fun NutritionSection(recipe: RecipeDetail) {
    val nutrients = recipe.nutrition?.nutrients ?: return

    val calories = nutrients.find { it.name.equals("Calories", true) }
    val protein = nutrients.find { it.name.equals("Protein", true) }
    val carbs = nutrients.find { it.name.equals("Carbohydrates", true) }
    val fat = nutrients.find { it.name.equals("Fat", true) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(halfMargin())
    ) {
        calories?.let {
            NutrientBox(
                "${it.amount?.toInt()} kcal",
                "calories",
                PinkLight,
                PinkDark,
                Modifier.weight(1f)
            )
        }

        protein?.let {
            NutrientBox(
                "${it.amount?.toInt()} g",
                "protein",
                BlueLight,
                BlueDark,
                Modifier.weight(1f)
            )
        }
        carbs?.let {
            NutrientBox(
                "${it.amount?.toInt()} g",
                "carbs",
                GreenLight,
                GreenDark,
                Modifier.weight(1f)
            )
        }
        fat?.let {
            NutrientBox(
                "${it.amount?.toInt()} g",
                "fat",
                OrangeLight,
                OrangeDark,
                Modifier.weight(1f)
            )
        }
    }
}