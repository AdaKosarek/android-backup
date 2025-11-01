package cz.petstore2025.model

data class ApiResponse(
    val code: Int,
    val type: String?,
    val message: String?
)
