package com.example.scheduleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.scheduleapp.data.GroupSC
import com.example.scheduleapp.repository.ScheduleDBRepository
import java.util.*

class GroupInfoDBViewModel : ViewModel() {
    private val groupRepository = ScheduleDBRepository.get()
    private val groupIdLiveData = MutableLiveData<UUID>()
    var groupLiveData: LiveData<GroupSC?> =
        Transformations.switchMap(groupIdLiveData) { groupId ->
            groupRepository.getGroup(groupId)
        }

    fun loadGroup(groupID: UUID) {
        groupIdLiveData.value = groupID
    }

    fun newGroup(group: GroupSC) {
        groupRepository.addGroup(group)
    }

    fun saveGroup(group: GroupSC) {
        groupRepository.updateGroup(group)
    }

    fun dropGroup(group: GroupSC) {
        groupRepository.deleteGroup(group)
    }

    fun getGroupID(): UUID {
        return groupRepository.groupID
    }

}