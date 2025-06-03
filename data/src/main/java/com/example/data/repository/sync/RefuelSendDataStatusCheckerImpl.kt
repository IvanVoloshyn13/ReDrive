package com.example.data.repository.sync

import com.example.domain.sync.SendDataStatusChecker
import com.example.localedatasource.room.daos.RefuelDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RefuelSendDataStatusCheckerImpl @Inject constructor(
    private val refuelDao: RefuelDao
) : SendDataStatusChecker {
    override fun shouldSend(key: String): Flow<Boolean> {
        return refuelDao.hasPending(key)
    }
}