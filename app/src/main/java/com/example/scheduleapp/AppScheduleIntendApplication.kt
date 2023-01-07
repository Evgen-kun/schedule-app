package com.example.scheduleapp

import android.app.Application
import android.content.Context
import com.example.scheduleapp.repository.ScheduleDBRepository

class AppScheduleIntendApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppScheduleIntendApplication? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        ScheduleDBRepository.initialize(this)
    }

}