package it.lexpon.nim.periphery.datatransferobject

import it.lexpon.nim.core.domainobject.GameInformation
import it.lexpon.nim.core.domainobject.GameStatus
import it.lexpon.nim.core.domainobject.MoveInformation
import it.lexpon.nim.core.domainobject.Player

data class GameStatusResponse(
        val gameStatus: GameStatus,
        val moveInformationResponse: MoveInformationResponse? = null,
        val leftSticks: Int,
        val winner: Player? = null,
        val message: String
) {
    constructor(gameInformation: GameInformation) : this(
            gameStatus = gameInformation.gameStatus,
            moveInformationResponse = gameInformation.moveInformation?.let { MoveInformationResponse(it) },
            leftSticks = gameInformation.leftSticks,
            winner = gameInformation.winner,
            message = gameInformation.message!!
    )
}

data class MoveInformationResponse(
        val pulledSticksByHuman: Int?,
        val pulledSticksByComputer: Int?
) {
    constructor(moveInformation: MoveInformation) : this(
            pulledSticksByHuman = moveInformation.pulledSticksByHuman,
            pulledSticksByComputer = moveInformation.pulledSticksByComputer
    )
}