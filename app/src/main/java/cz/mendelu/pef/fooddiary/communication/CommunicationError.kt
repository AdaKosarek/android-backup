package cz.mendelu.pef.fooddiary.communication

data class CommunicationError(
    val code: Int,
    val message: String? = null
)
