package com.example.scheduleapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.scheduleapp.data.Schedule
import com.example.scheduleapp.database.ScheduleDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "schedule-database"

class ScheduleDBRepository private constructor(context: Context) {

    companion object {
        private var INSTANCE: ScheduleDBRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ScheduleDBRepository(context)
            }
        }
        fun get(): ScheduleDBRepository {
            return INSTANCE ?: throw IllegalStateException("ScheduleDBRepository must be initialized")
        }
    }

    private val database : ScheduleDatabase = Room.databaseBuilder(
        context.applicationContext,
        ScheduleDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val scheduleDao = database.scheduleDao()

    fun getSchedules(): LiveData<List<Schedule>> = scheduleDao.getSchedules()
    fun getSchedule(id: UUID): LiveData<Schedule?> = scheduleDao.getSchedule(id)

    private val executor = Executors.newSingleThreadExecutor()
    fun updateSchedule(schedule: Schedule) {
        executor.execute {
            scheduleDao.updateSchedule(schedule)
        }
    }
    fun addSchedule(schedule: Schedule) {
        executor.execute {
            scheduleDao.addSchedule(schedule)
        }
    }


}