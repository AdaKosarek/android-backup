package cz.petstore2025.communication

data class CommunicationError(
    val code: Int,
    val message: String? = null
)
