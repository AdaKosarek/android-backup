package cz.petstore2025.model

data class Pet(
    val id: Long? = null,
    val category: Category? = null,
    val name: String? = null,
    val photoUrls: List<String>? = null,
    val tags: List<Tag>? = null,
    val status: String? = null
)
