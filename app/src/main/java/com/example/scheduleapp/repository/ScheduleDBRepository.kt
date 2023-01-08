package com.example.scheduleapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.scheduleapp.data.Group
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

    fun getGroups(): LiveData<List<Group>> = scheduleDao.getGroups()
    fun getGroup(id: UUID): LiveData<Group?> = scheduleDao.getGroup(id)

    fun getSchedules(): LiveData<List<Schedule>> = scheduleDao.getSchedules()
    fun getSortedSchedulesByDiscipline(): LiveData<List<Schedule>> = scheduleDao.getSortedSchedulesByDiscipline()
    fun getSortedSchedulesByTeacherName(): LiveData<List<Schedule>> = scheduleDao.getSortedSchedulesByTeacherName()
    fun getSortedSchedulesByPosition(): LiveData<List<Schedule>> = scheduleDao.getSortedSchedulesByPosition()
    fun getSortedSchedulesByBuilding(): LiveData<List<Schedule>> = scheduleDao.getSortedSchedulesByBuilding()
    fun getSortedSchedulesByDate(): LiveData<List<Schedule>> = scheduleDao.getSortedSchedulesByDate()
    fun getSortedSchedulesByDuration(): LiveData<List<Schedule>> = scheduleDao.getSortedSchedulesByDuration()
    fun getSortedSchedulesByClassroom(): LiveData<List<Schedule>> = scheduleDao.getSortedSchedulesByClassroom()
    //fun getSortedSchedules(column: String): LiveData<List<Schedule>> = scheduleDao.getSortedSchedules(column)

    fun getSchedule(id: UUID): LiveData<Schedule?> = scheduleDao.getSchedule(id)
    fun getScheduleElements(id: UUID): LiveData<Date> = scheduleDao.getScheduleElements(id)

    private val executor = Executors.newSingleThreadExecutor()
    fun updateGroup(group: Group) {
        executor.execute {
            scheduleDao.updateGroup(group)
        }
    }
    fun addGroup(group: Group) {
        executor.execute {
            scheduleDao.addGroup(group)
        }
    }
    fun deleteGroup(group: Group) {
        executor.execute {
            scheduleDao.deleteGroup(group)
        }
    }

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
    fun deleteSchedule(schedule: Schedule) {
        executor.execute {
            scheduleDao.deleteSchedule(schedule)
        }
    }

}