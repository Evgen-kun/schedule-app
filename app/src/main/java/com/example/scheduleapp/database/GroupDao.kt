package com.example.scheduleapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.scheduleapp.data.GroupSC
import java.util.*

@Dao
interface GroupDao {
    @Query("SELECT * FROM GroupSC")
    fun getGroups(): LiveData<List<GroupSC>>
    @Query("SELECT * FROM GroupSC WHERE id=(:id)")
    fun getGroup(id: UUID): LiveData<GroupSC?>
    @Update
    fun updateGroup(group: GroupSC)
    @Insert
    fun addGroup(group: GroupSC)
    @Delete
    fun deleteGroup(group: GroupSC)
}