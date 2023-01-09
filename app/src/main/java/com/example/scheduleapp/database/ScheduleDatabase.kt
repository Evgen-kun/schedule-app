package com.example.scheduleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scheduleapp.data.GroupSC
import com.example.scheduleapp.data.Schedule

@Database(entities = [ GroupSC::class, Schedule::class ], version = 1)
@TypeConverters(ScheduleTypeConverters::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun scheduleDao(): ScheduleDao
}