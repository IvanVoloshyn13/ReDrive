package com.example.redrive.core

import kotlinx.coroutines.flow.Flow

interface NetworkStatusProvider {

    fun networkStatusFlow(): Flow<NetworkStatus>
}

enum class NetworkStatus {
    CONNECTED, LOST
}