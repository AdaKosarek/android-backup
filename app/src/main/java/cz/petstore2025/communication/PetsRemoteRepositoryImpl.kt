package cz.petstore2025.communication

import android.content.Context
import android.net.Uri
import cz.petstore2025.model.ApiResponse
import cz.petstore2025.model.Pet
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class PetsRemoteRepositoryImpl @Inject constructor(
    private val api: PetsAPI,
    @ApplicationContext private val context: Context) : IPetsRemoteRepository {

    override suspend fun findByStatus(status: String): CommunicationResult<List<Pet>> {
        return processResponse {
            api.findByStatus(status)
        }

    }

    override suspend fun findPetById(petId: Long): CommunicationResult<Pet> {
        return processResponse {
            api.findPetById(petId)
        }
    }

    //vymazani
    override suspend fun deletePet(petId: Long): CommunicationResult<Unit> {
        return processResponse {
            api.deletePet(petId)
        }
    }

    //pridani zvirete
    override suspend fun addPet(pet: Pet): CommunicationResult<Pet> {
        return processResponse {
            api.addPet(pet)
        }
    }

    override suspend fun uploadPetImage(
        petId: Long,
        imageUri: Uri,
        additionalMetadata: String?
    ): CommunicationResult<ApiResponse> = withContext(Dispatchers.IO) {
        return@withContext try {
            // Otevřeme stream k souboru z úložiště
            val inputStream = context.contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                return@withContext CommunicationResult.Error(
                    CommunicationError(400, "Cannot open file stream.")
                )
            }

            //cache
            val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            //tělo requestu
            val metadataBody = additionalMetadata?.toRequestBody("text/plain".toMediaTypeOrNull())
            val fileRequestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", tempFile.name, fileRequestBody)

            processResponse {
                api.uploadPetImage(petId, metadataBody, filePart)
            }
        } catch (e: Exception) {
            CommunicationResult.Exception(e)
        }
    }
}
