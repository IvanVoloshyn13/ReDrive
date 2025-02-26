package com.example.redrive

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ReDriveApp : Application() {

    @Inject
    lateinit var firebaseApp: FirebaseApp

}