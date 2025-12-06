package cz.mendelu.pef.fooddiary.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin

@Composable
fun InstructionSection(recipe: RecipeDetail) {
    val steps =
        recipe.analyzedInstructions?.firstOrNull()?.steps
            ?: emptyList()

    if (steps.isEmpty()) {
        Text(
            text = recipe.instructions ?: "",
            modifier = Modifier.padding(horizontal = basicMargin()),
            lineHeight = 22.sp
        )
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        steps.forEach { step ->
            Text(
                text = "${step.number}. ${step.step}",
                modifier = Modifier.padding(vertical = 6.dp),
                lineHeight = 22.sp
            )
        }
    }
}
