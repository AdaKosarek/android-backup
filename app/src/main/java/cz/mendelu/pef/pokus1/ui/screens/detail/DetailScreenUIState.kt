package cz.mendelu.pef.pokus1.ui.screens.detail

import cz.mendelu.pef.pokus1.model.PackageDTO
import cz.mendelu.pef.pokus1.model.Stop

data class DetailScreenUIState(
    val loading: Boolean = true,
    val stop: Stop? = null,
    val packageCount: Int = 0,
    val totalWeight: Double = 0.0,
    val error: DetailScreenError? = null,
    val packages: List<PackageDTO> = emptyList()
)