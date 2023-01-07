package com.example.scheduleapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.scheduleapp.data.Schedule
import java.util.*

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getSchedules(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule WHERE id=(:id)")
    fun getSchedule(id: UUID): LiveData<Schedule?>
    @Update
    fun updateSchedule(student: Schedule)
    @Insert
    fun addSchedule(student: Schedule)
}