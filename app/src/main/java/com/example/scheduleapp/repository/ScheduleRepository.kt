package com.example.scheduleapp.repository

import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.scheduleapp.AppScheduleIntendApplication
import com.example.scheduleapp.data.Schedule
import com.example.scheduleapp.data.ScheduleList
import com.google.gson.Gson

class ScheduleRepository {

    companion object {
        private var INSTANCE : ScheduleRepository?=null

        fun getInstance(): ScheduleRepository {
            if (INSTANCE == null) {
                INSTANCE = ScheduleRepository()
            }
            return INSTANCE?:
            throw IllegalStateException("Репозиторий ScheduleRepository не инициализирован")
        }
    }

    init {
        loadSchedules()
    }

    var schedulesList: MutableLiveData<ScheduleList> = MutableLiveData()
    var schedule: MutableLiveData<Schedule> = MutableLiveData()

    fun loadSchedules() {
        val jsonString =
            PreferenceManager.getDefaultSharedPreferences(AppScheduleIntendApplication.applicationContext())
                .getString("schedules", null)
        if (!jsonString.isNullOrBlank()) {
            val sc = Gson().fromJson(jsonString, ScheduleList::class.java)
            if (sc != null)
                this.schedulesList.postValue(sc)
        }
    }

    fun saveSchedules() {
        val gson = Gson()
        val jsonSchedules = gson.toJson(schedulesList.value)
        val preference =
            PreferenceManager.getDefaultSharedPreferences(AppScheduleIntendApplication.applicationContext())
        preference.edit().apply {
            putString("schedules", jsonSchedules)
            apply()
        }
    }

    fun setCurrentSchedule(position: Int) {
        if (schedulesList.value == null || schedulesList.value!!.items == null ||
            position < 0 || (schedulesList.value?.items?.size!! <= position))
            return
        schedule.postValue(schedulesList.value?.items!![position])
    }

    fun setCurrentSchedule(schedule: Schedule) {
        this.schedule.postValue(schedule)
    }

    fun addSchedule(schedule: Schedule) {
        var schedulesListTmp = schedulesList
        if (schedulesListTmp.value == null)
            schedulesListTmp.value = ScheduleList()
        schedulesListTmp.value!!.items.add(schedule)
        schedulesList.postValue(schedulesListTmp.value)
    }

    fun getPosition(schedule: Schedule) : Int = schedulesList.value?.items?.indexOfFirst {
        it.id == schedule.id } ?: -1

    fun updateSchedule(schedule: Schedule) {
        var schedulesListTmp = schedulesList
        val position = getPosition(schedule)
        if (schedulesListTmp.value == null || position < 0)
            addSchedule(schedule)
        else {
            schedulesListTmp.value!!.items[position] = schedule
            schedulesList.postValue(schedulesListTmp.value)
        }
    }
    fun deleteSchedule(schedule: Schedule) {
        var schedulesListTmp = schedulesList
        if (schedulesListTmp.value!!.items.remove(schedule)) {
            schedulesList.postValue(schedulesListTmp.value)
        }
    }

    fun deleteScheduleCurrent() {
        if (schedule.value!=null)
            deleteSchedule(schedule.value!!)
    }

    fun newSchedule() {
        schedule.postValue(Schedule())
    }

}