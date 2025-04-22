package com.example.redrive.core

import com.example.domain.AppException

interface AppStringResProvider {
    fun provideStringResByException(e:AppException): String
}