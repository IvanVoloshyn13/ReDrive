package com.example.data.repository.sync

import com.example.domain.sync.SendDataStatusChecker
import com.example.localedatasource.room.daos.SettingsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnitsPrefSendDataStatusCheckerImpl @Inject constructor(
    private val settingsDao: SettingsDao,
) : SendDataStatusChecker {

    override fun shouldSend(key: String): Flow<Boolean> {
        return settingsDao.hasPending(key)
    }
}