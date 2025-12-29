package cz.mendelu.pef.fooddiary.analyzer

import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions

class FoodImageLabelAnalyzer(
    private val onResult: (label: String?, confidence: Float?) -> Unit
) {

    private val labeler = ImageLabeling.getClient(
        CustomImageLabelerOptions.Builder(
            LocalModel.Builder()
                .setAssetFilePath("meals1.tflite")
                .build()
        )
            .setConfidenceThreshold(0.2f)//
            .build()
    )

    fun process(image: InputImage) {
        labeler.process(image)
            .addOnSuccessListener { labels ->
                val best = labels.maxByOrNull { it.confidence }

                if (best != null) {
                    onResult(best.text, best.confidence)
                } else {
                    onResult(null, null)
                }
            }
            .addOnFailureListener {
                onResult(null, null)
            }
    }
}
