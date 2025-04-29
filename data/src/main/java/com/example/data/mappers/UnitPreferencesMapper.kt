package com.example.data.mappers

import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.localedatasource.appPreferencesFromAssets.models.DefaultPreferencesResponse
import com.example.localedatasource.room.entity.UnitPreferencesEntity

interface UnitPreferencesMapper {

    fun UnitPreferencesEntity.toPreferences(): UnitsPreferencesAbbreviation

    fun UnitsPreferencesAbbreviation.toEntity(vehicleId:Long?=null): UnitPreferencesEntity

    fun DefaultPreferencesResponse.toPreferences(): UnitsPreferencesAbbreviation
}