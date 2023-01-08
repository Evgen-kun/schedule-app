package com.example.scheduleapp

import androidx.lifecycle.ViewModel
import com.example.scheduleapp.repository.ScheduleDBRepository

class GroupListDBViewModel : ViewModel() {
    private val groupRepository = ScheduleDBRepository.get()
    val groupListLiveData = groupRepository.getGroups()
}