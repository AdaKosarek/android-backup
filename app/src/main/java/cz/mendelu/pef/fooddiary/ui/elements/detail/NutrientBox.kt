package cz.mendelu.pef.fooddiary.ui.elements.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fooddiary.ui.theme.GrayText

@Composable
fun NutrientBox(
    value: String,
    label: String,
    background: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(background, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = textColor,
            fontSize = 18.sp
        )
        Text(
            text = label,
            color = GrayText,
            fontSize = 13.sp
        )
    }
}
