package com.example.redrive.core

import android.content.Context
import com.example.domain.AppException
import com.example.domain.AuthException
import com.example.domain.UserException
import com.example.domain.VehicleException
import com.example.redrive.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppStringResProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppStringResProvider {
    override fun provideStringRes(e: AppException): String {
        return when (e) {
            is VehicleException -> fromVehicleException(e)
            is UserException -> fromUserException(e)
            is AuthException -> fromAuthException(e)
            else -> {
                TODO()
            }
        }
    }

    private fun fromVehicleException(e: VehicleException): String {
        return when (e) {
            is VehicleException.IsCurrentVehicleException -> context.resources.getString(R.string.is_current_vehicle_exception)
            is VehicleException.NoCurrentVehicleException -> context.resources.getString(R.string.no_current_vehicle_exception_message)
            is VehicleException.InvalidOdometerValueException -> context.resources.getString(
                R.string.invalid_odometer_value_exception_message,
                e.initialValue
            )

        }
    }

    private fun fromUserException(e: UserException): String {
        return when (e) {
            is UserException.NoUserDetectedException -> context.resources.getString(R.string.no_user_detected_exception_message)
        }
    }

    private fun fromAuthException(e: AuthException): String {
        return when (e) {
            is AuthException.AuthenticationFailed -> context.resources.getString(R.string.firebase_auth_error)
            is AuthException.InvalidPasswordException -> context.resources.getString(R.string.invalid_password)
            is AuthException.UnknownException -> context.resources.getString(R.string.unknown_error_firebase)
            is AuthException.UserAlreadyExistsException -> context.resources.getString(R.string.user_collision)
            is AuthException.UserNotFoundException -> context.resources.getString(R.string.no_user_detected)
        }
    }
}
