package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.Player

// TODO implement
data class PullSticksResponse(
        val leftSticks: Int,
        val winner: Player? = null,
        val message: String
)