package com.example.scheduleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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
}