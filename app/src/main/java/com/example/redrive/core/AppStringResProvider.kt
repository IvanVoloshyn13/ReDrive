package com.example.redrive.core

import com.example.domain.AppException

interface AppStringResProvider {
    fun provideStringRes(e:AppException): String
}