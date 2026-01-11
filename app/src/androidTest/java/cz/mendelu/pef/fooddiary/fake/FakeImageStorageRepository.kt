package cz.mendelu.pef.fooddiary.fake
import android.net.Uri
import cz.mendelu.pef.fooddiary.utils.IImageStorageRepository
import javax.inject.Inject

class FakeImageStorageRepository @Inject constructor() : IImageStorageRepository {

    override fun saveMealPhoto(sourceUri: Uri): String {
        return sourceUri.toString()
    }

    override fun deletePhoto(photoUri: String) {}
}
