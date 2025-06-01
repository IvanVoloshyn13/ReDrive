package com.example.data.repository.sync

import com.example.domain.sync.SendDataStatusChecker
import com.example.localedatasource.room.daos.VehiclesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VehiclesSendDataStatusCheckerImpl @Inject constructor(
    private val vehiclesDao: VehiclesDao
) : SendDataStatusChecker {

    override fun shouldSend(key: String): Flow<Boolean> {
        return vehiclesDao.hasPendingVehicles(key)
    }

}