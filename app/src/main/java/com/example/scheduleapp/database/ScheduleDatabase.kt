package com.example.scheduleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scheduleapp.data.Group
import com.example.scheduleapp.data.Schedule

@Database(entities = [ Schedule::class, Group::class ], version = 1)
@TypeConverters(ScheduleTypeConverters::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}