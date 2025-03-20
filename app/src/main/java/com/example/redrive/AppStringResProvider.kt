package com.example.redrive

import com.example.domain.AppException

interface AppStringResProvider {
    fun provideStringRes(e:AppException): String
}