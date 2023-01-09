package com.example.scheduleapp

import androidx.lifecycle.ViewModel
import com.example.scheduleapp.repository.ScheduleDBRepository

class ScheduleListDBViewModel : ViewModel() {
    private val scheduleRepository = ScheduleDBRepository.get()
    val scheduleListLiveData = scheduleRepository.getSchedules(scheduleRepository.groupID)
}