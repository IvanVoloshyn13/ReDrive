package com.example.redrive

import android.content.Context
import com.example.domain.AppException
import com.example.domain.VehicleException
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppStringResProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppStringResProvider {
    override fun provideStringRes(e: AppException): String {
        return when (e) {
            is VehicleException -> fromVehicleException(e)
            else -> {
                TODO()
            }
        }
    }

    private fun fromVehicleException(e: VehicleException): String {
        return when (e) {
            is VehicleException.IsCurrentVehicleException -> context.resources.getString(R.string.is_current_vehicle_exception)
            is VehicleException.NoCurrentVehicleException -> TODO()
        }
    }
}