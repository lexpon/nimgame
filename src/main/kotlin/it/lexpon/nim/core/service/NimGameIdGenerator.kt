package it.lexpon.nim.core.service

import org.springframework.stereotype.Component

@Component
class NimGameIdGenerator {
    private var currentId: Int = 0

    fun getNewId(): Int {
        currentId = currentId + 1
        return currentId
    }
}