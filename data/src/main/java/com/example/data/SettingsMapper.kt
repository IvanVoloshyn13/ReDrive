package com.example.data

import com.example.domain.model.Settings
import com.example.localedatasource.inMemoryAppSettings.models.DefaultSettingsResponse
import com.example.localedatasource.room.entity.SettingsEntity

interface SettingsMapper {

    fun SettingsEntity.toSettings(): Settings

    fun Settings.toEntity(vehicleId:Long?=null): SettingsEntity

    fun DefaultSettingsResponse.toSettings(): Settings
}