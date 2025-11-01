package cz.petstore2025.model

data class Order(
    val id: Long? = null,
    val petId: Long? = null,
    val quantity: Int? = null,
    val shipDate: String? = null,
    val status: String? = null,
    val complete: Boolean? = null
)

