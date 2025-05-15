package com.example.firebase.auth.models

data class FirebaseUserProfile(
    val fullName:String,
    val credentials: FbAuthCredentials
)
