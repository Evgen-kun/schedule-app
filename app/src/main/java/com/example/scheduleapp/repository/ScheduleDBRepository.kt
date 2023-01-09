package com.example.scheduleapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.scheduleapp.MyConstants
import com.example.scheduleapp.data.GroupSC
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

    private val groupDao = database.groupDao()
    private val scheduleDao = database.scheduleDao()
    var groupID: UUID = UUID.randomUUID()

    fun getGroups(): LiveData<List<GroupSC>> = groupDao.getGroups()
    fun getGroup(id: UUID): LiveData<GroupSC?> {
        val gr = groupDao.getGroup(id)
        Log.d(MyConstants.TAG, "groupID (2) IS $id")
        Log.d(MyConstants.TAG, "group IS ${gr.value}")
        setCurrentGroupID(id)
        return gr
    }

    fun getSchedules(groupID: UUID = this.groupID): LiveData<List<Schedule>> = scheduleDao.getSchedules(groupID)
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
    fun updateGroup(group: GroupSC) {
        executor.execute {
            groupDao.updateGroup(group)
        }
    }
    fun addGroup(group: GroupSC) {
        executor.execute {
            groupDao.addGroup(group)
        }
    }
    fun deleteGroup(group: GroupSC) {
        executor.execute {
            groupDao.deleteGroup(group)
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

    private fun setCurrentGroupID(groupID: UUID) {
        this.groupID = groupID
    }

}