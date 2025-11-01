package cz.petstore2025.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import cz.petstore2025.R
import cz.petstore2025.model.Pet

@Composable
fun PetRow(
    pet: Pet,
    onClick: () -> Unit
) {
    val fallbackImage = painterResource(id = R.drawable.un_dog)

    val petName = pet.name?.takeIf { it.isNotBlank() } ?: "Pet"
    val validPhotoUrl = pet.photoUrls
        ?.firstOrNull { url ->
            url.isNotBlank() &&
                    url.lowercase() != "string" &&
                    (url.startsWith("http://") || url.startsWith("https://"))
        }

    val painter = if (validPhotoUrl != null) {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(validPhotoUrl)
                .crossfade(true)
                .error(R.drawable.un_dog)
                .fallback(R.drawable.un_dog)
                .build()
        )
    } else {
        fallbackImage
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = petName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
