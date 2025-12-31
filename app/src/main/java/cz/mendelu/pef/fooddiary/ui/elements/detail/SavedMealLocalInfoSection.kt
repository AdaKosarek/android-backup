package cz.mendelu.pef.fooddiary.ui.elements.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.ui.screens.saveddetail.SavedDetailActions
import cz.mendelu.pef.fooddiary.ui.screens.saveddetail.SavedDetailScreenUIState
import cz.mendelu.pef.fooddiary.ui.theme.ChipBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedMealLocalInfoSection(
    meal: SavedMeal,
    actions: SavedDetailActions,
    isEditing: Boolean,
    state: SavedDetailScreenUIState
) {
    val hasLocalContent =
        !meal.customName.isNullOrBlank() || !meal.userNote.isNullOrBlank() ||
                meal.placeName != null ||
                meal.hasLocation

    if (!hasLocalContent) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!isEditing) {
                meal.customName?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            } else {
                OutlinedTextField(
                    value = state.editName,
                    onValueChange = actions::onEditNameChange,
                    isError = state.editName.isBlank(),
                    singleLine = true,
                    label = { Text(stringResource(R.string.custom_name)) }
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = actions::onToggleEdit) {
                Icon(
                    imageVector =
                        if (isEditing) Icons.Outlined.Close
                        else Icons.Outlined.Edit,
                    contentDescription = null,
                    tint = GrayText
                )
            }


            IconButton(onClick = actions::onDeleteMeal) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = null,
                tint = GrayText,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = SimpleDateFormat(
                    "dd. MM. yyyy",
                    Locale.getDefault()
                ).format(Date(meal.savedTimestamp)),
                color = GrayText,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!isEditing) {
            if (meal.placeName != null || meal.hasLocation) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ChipBackground, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Place,
                        contentDescription = null,
                        tint = OrangePrimary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        meal.placeName?.let {
                            Text(
                                text = it
                            )
                        }

                        if (meal.hasLocation) {
                            Text(
                                text = stringResource(R.string.has_location),
                                color = OrangePrimary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }else{
            OutlinedTextField(
                value = state.editPlaceName,
                onValueChange = actions::onEditPlaceNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.place_optional)) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        if (!isEditing) {
            meal.userNote?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = stringResource(R.string.my_note),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = it,
                    lineHeight = 22.sp,
                    color = GrayText
                )
            }
        }else {
            OutlinedTextField(
                value = state.editNote,
                onValueChange = actions::onEditNoteChange,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                label = { Text(stringResource(R.string.note_optional)) }
            )
        }

    }
}