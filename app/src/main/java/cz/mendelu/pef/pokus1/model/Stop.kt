package cz.mendelu.pef.pokus1.model

data class Stop(
    val id: Long? = null,
    val name: String? = null,
    val street: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)