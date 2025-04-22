package com.example.redrive.core.logTextFormatter

import android.content.Context
import com.example.redrive.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RefuelMessageFromResProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RefuelMessageFromResProvider {
    override fun getMessage(odometer: String, date: String): String {
        return context.getString(R.string.refuel_message, odometer, date)
    }
}