package cz.mendelu.pef.fooddiary.utils

import android.net.Uri

interface IImageStorageRepository {
    fun saveMealPhoto(sourceUri: Uri): String
    fun deletePhoto(photoUri: String)
}