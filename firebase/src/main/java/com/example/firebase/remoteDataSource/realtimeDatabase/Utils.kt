package com.example.firebase.remoteDataSource.realtimeDatabase

object Constants {
    const val UPLOAD_AT = "uploadAt"
}

fun getCurrentUserPath(userId: String) = "user-$userId"