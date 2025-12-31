package cz.mendelu.pef.fooddiary.utils
import java.io.File
import java.util.UUID
import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun saveMealPhoto(sourceUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(sourceUri)
            ?: throw IllegalStateException("Cannot open input stream")

        val fileName = "meal_${UUID.randomUUID()}.jpg"
        val photosDir = File(context.filesDir, "meal_photos").apply {
            if (!exists()) mkdirs()
        }

        val destinationFile = File(photosDir, fileName)

        inputStream.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return destinationFile.toURI().toString()
    }

    fun deletePhoto(photoUri: String) {
        runCatching {
            File(URI(photoUri)).delete()
        }
    }
}
