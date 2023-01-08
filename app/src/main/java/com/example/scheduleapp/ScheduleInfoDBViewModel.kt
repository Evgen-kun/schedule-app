package com.example.scheduleapp

import android.util.Log
import androidx.lifecycle.*
import com.example.scheduleapp.data.Schedule
import com.example.scheduleapp.repository.ScheduleDBRepository
import java.util.*

class ScheduleInfoDBViewModel: ViewModel() {
    private val scheduleRepository = ScheduleDBRepository.get()
    private val scheduleIdLiveData = MutableLiveData<UUID>()
    var scheduleLiveData: LiveData<Schedule?> =
        Transformations.switchMap(scheduleIdLiveData) { scheduleId ->
            scheduleRepository.getSchedule(scheduleId)
        }

    fun loadSchedule(scheduleID: UUID) {
        scheduleIdLiveData.value = scheduleID
    }

    fun newSchedule(schedule: Schedule) {
        scheduleRepository.addSchedule(schedule)
    }

    fun saveSchedule(schedule: Schedule) {
        scheduleRepository.updateSchedule(schedule)
    }

    fun dropSchedule(schedule: Schedule) {
        scheduleRepository.deleteSchedule(schedule)
    }


    fun sortSchedulesByDiscipline(): LiveData<List<Schedule>> {
        return scheduleRepository.getSortedSchedulesByDiscipline()
    }
    fun sortSchedulesByPosition(): LiveData<List<Schedule>> {
        return scheduleRepository.getSortedSchedulesByPosition()
    }
    fun sortSchedulesByTeacherName(): LiveData<List<Schedule>> {
        return scheduleRepository.getSortedSchedulesByTeacherName()
    }
    fun sortSchedulesByDate(): LiveData<List<Schedule>> {
        return scheduleRepository.getSortedSchedulesByDate()
    }
    fun sortSchedulesByBuilding(): LiveData<List<Schedule>> {
        return scheduleRepository.getSortedSchedulesByBuilding()
    }
    fun sortSchedulesByClassroom(): LiveData<List<Schedule>> {
        return scheduleRepository.getSortedSchedulesByClassroom()
    }
    fun sortSchedulesByDuration(): LiveData<List<Schedule>> {
        return scheduleRepository.getSortedSchedulesByDuration()
    }

}