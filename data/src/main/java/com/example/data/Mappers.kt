package com.example.data

import com.example.domain.AppException
import com.example.domain.AuthException
import com.example.domain.model.Settings
import com.example.domain.model.User
import com.example.domain.model.UserAuthCredentials
import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.firebase.FbUserAuthCredentials
import com.example.localedatasource.inMemoryAppSettings.models.DefaultAppSettingsResponse
import com.example.localedatasource.room.entity.SettingsEntity
import com.example.localedatasource.room.entity.UserEntity
import com.example.localedatasource.room.entity.VehicleEntity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUserEntity(): UserEntity {
    return UserEntity(
        id = uid,
        email = email!!,
        fullName = displayName!!
    )
}

fun FirebaseUser.toUser(): User {
    return User(
        uUid = this.uid,
        fullName = this.displayName ?: ""
    )
}

fun UserEntity.toUser(): User {
    return User(
        uUid = this.id,
        fullName = this.fullName
    )
}

fun UserAuthCredentials.toFbUserAuthCredentials(): FbUserAuthCredentials {
    return FbUserAuthCredentials(
        password = password,
        email = email,
        fullName = fullName
    )
}

fun Vehicle.toEntity(uUid: String): VehicleEntity {
    return VehicleEntity(
        id = this.id,
        userId = uUid,
        name = this.name,
        vehicleType = type.name,
        initialOdometerValue = this.initialOdometerValue
    )
}

fun VehicleEntity.toVehicle(): Vehicle {
    return Vehicle(
        id = this.id,
        name = this.name,
        initialOdometerValue = this.initialOdometerValue,
        type = VehicleType.valueOf(this.vehicleType)
    )
}

fun DefaultAppSettingsResponse.toSettings(): Settings {
    return Settings(
        currencyAbbr = this.currency.abbreviation,
        capacityAbbr = this.capacity.abbreviation,
        distanceAbbr = this.distance.abbreviation,
        avgConsumptionAbbr = this.avgConsumption.abbreviation,
        dateFormatPattern = this.dateFormat.pattern
    )
}

fun SettingsEntity.toSettings(): Settings {
    return Settings(
        id = this.id,
        currencyAbbr = this.currency,
        capacityAbbr = this.capacity,
        distanceAbbr = this.distance,
        avgConsumptionAbbr = this.avgConsumption,
        dateFormatPattern = this.dateFormatPatter
    )
}

fun Settings.toEntity(vehicleId: Long? = null): SettingsEntity {
    return SettingsEntity(
        id = this.id,
        vehicleId = vehicleId ?: 0,
        currency = this.currencyAbbr,
        capacity = this.capacityAbbr,
        distance = this.distanceAbbr,
        avgConsumption = this.avgConsumptionAbbr,
        dateFormatPatter = this.dateFormatPattern
    )
}

fun FirebaseException.toAppAuthException(): AppException {
    return when (this) {
        is FirebaseAuthInvalidCredentialsException -> AuthException.InvalidPasswordException()
        is FirebaseAuthInvalidUserException -> AuthException.UserNotFoundException()
        is FirebaseAuthUserCollisionException -> AuthException.UserAlreadyExistsException()
        is FirebaseAuthException -> AuthException.AuthenticationFailed()
        else -> AuthException.UnknownException()

    }

}