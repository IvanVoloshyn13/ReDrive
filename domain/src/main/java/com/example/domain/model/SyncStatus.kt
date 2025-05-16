package com.example.domain.model

enum class SyncStatus(val code: Int) {
    SYNCED(1),
    PENDING(0),
    FAILED(-1);

    companion object {
        fun fromCode(code: Int): SyncStatus {
            return entries.firstOrNull { it.code == code } ?: PENDING
        }
    }
}