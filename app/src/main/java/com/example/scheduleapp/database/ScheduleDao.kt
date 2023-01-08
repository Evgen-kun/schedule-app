package com.example.scheduleapp.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.scheduleapp.data.Schedule
import java.util.*

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getSchedules(): LiveData<List<Schedule>>

    //@Query("SELECT * FROM schedule ORDER BY :column ASC")
    //fun getSchedules(column: String): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule ORDER BY discipline ASC")
    fun getSortedSchedulesByDiscipline(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule ORDER BY teacherName ASC")
    fun getSortedSchedulesByTeacherName(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule ORDER BY position ASC")
    fun getSortedSchedulesByPosition(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule ORDER BY building ASC")
    fun getSortedSchedulesByBuilding(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule ORDER BY date ASC")
    fun getSortedSchedulesByDate(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule ORDER BY duration ASC")
    fun getSortedSchedulesByDuration(): LiveData<List<Schedule>>
    @Query("SELECT * FROM schedule ORDER BY classroom ASC")
    fun getSortedSchedulesByClassroom(): LiveData<List<Schedule>>

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