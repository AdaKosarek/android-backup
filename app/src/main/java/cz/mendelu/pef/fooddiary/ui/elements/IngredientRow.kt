package cz.mendelu.pef.fooddiary.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fooddiary.model.Ingredient
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin

@Composable
fun IngredientRow(ingredient: Ingredient) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = basicMargin(), vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = false,
            onCheckedChange = {},
        )
        Text(
            ingredient.name ?: "",
            modifier = Modifier.weight(1f),
            fontSize = 15.sp
        )
        Text(
            text = "${ingredient.amount ?: ""} ${ingredient.unit ?: ""}",
            color = GrayText
        )
    }
}
