package cz.petstore2025.ui.elements

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cz.petstore2025.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoUrlPicker(
    selectedUris: List<Uri>,
    onPhotoUrisChange: (List<Uri>) -> Unit,
    modifier: Modifier = Modifier
) {
    val sampleImageUrls = listOf(
        "https://images.unsplash.com/photo-1425082661705-1834bfd09dca",
        "https://images.unsplash.com/photo-1600865869069-638935377a06",
        "https://images.unsplash.com/photo-1558788353-f76d92427f16",
        "https://images.unsplash.com/photo-1570741066052-817c6de995c8",
        "https://images.unsplash.com/photo-1518717758536-85ae29035b6d",
        "https://images.unsplash.com/photo-1585110396000-c9ffd4e4b308",
        "https://images.unsplash.com/photo-1523837006673-1ad9ddfcbc0c",
        "https://images.unsplash.com/photo-1512087499053-023f060e2cea",
        "https://images.unsplash.com/photo-1574158622682-e40e69881006",
        "https://images.unsplash.com/photo-1548767797-d8c844163c4c"
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.photos),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(sampleImageUrls) { url ->
                val uri = Uri.parse(url)
                val isSelected = selectedUris.contains(uri)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            val newSelection = if (isSelected) {
                                selectedUris - uri
                            } else {
                                if (selectedUris.size < 3) selectedUris + uri else selectedUris
                            }
                            onPhotoUrisChange(newSelection)
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                }
            }
        }


    }
}



