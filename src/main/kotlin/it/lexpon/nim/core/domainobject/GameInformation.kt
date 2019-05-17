package it.lexpon.nim.core.domainobject

data class GameInformation(
        val gameStatus: GameStatus,
        val moveInformation: MoveInformation? = null,
        val leftSticks: Int,
        val winner: Player? = null,
        val message: String? = ""
)

data class MoveInformation(
        val pulledSticksByHuman: Int? = null,
        val pulledSticksByComputer: Int? = null
) {

    data class Builder(
            private var pulledSticksByHuman: Int? = null,
            private var pulledSticksByComputer: Int? = null
    ) {
        fun pulledSticksByHuman(pulledSticksByHuman: Int) = apply { this.pulledSticksByHuman = pulledSticksByHuman }

        fun pulledSticksByComputer(pulledSticksByComputer: Int) = apply { this.pulledSticksByComputer = pulledSticksByComputer }

        fun build(): MoveInformation =
                MoveInformation(
                        pulledSticksByHuman = pulledSticksByHuman,
                        pulledSticksByComputer = pulledSticksByComputer
                )
    }

}