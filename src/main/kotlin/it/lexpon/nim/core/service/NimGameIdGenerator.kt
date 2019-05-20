package it.lexpon.nim.core.service

object NimGameIdGenerator {
    private var currentId: Int = 0

    fun getNewId(): Int {
        currentId = currentId + 1
        return currentId
    }
}