package com.example.scheduleapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.scheduleapp.data.Schedule
import java.util.*

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getSchedules(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule WHERE id=(:id)")
    fun getSchedule(id: UUID): LiveData<Schedule?>
    @Query("SELECT date FROM schedule WHERE id=(:id)")
    fun getScheduleElements(id: UUID): LiveData<Date>
    @Update
    fun updateSchedule(schedule: Schedule)
    @Insert
    fun addSchedule(schedule: Schedule)
    @Delete
    fun deleteSchedule(schedule: Schedule)
}