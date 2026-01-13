package cz.mendelu.pef.pokus1.communication

data class CommunicationError(
    val code: Int,
    val message: String? = null
)
