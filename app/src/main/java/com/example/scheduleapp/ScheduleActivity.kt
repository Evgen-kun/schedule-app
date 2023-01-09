package com.example.scheduleapp

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.scheduleapp.MyConstants.EXTRA_GROUP_ID
import com.example.scheduleapp.data.Schedule
import com.example.scheduleapp.repository.ScheduleRepository
import java.util.*


class ScheduleActivity : AppCompatActivity(), ScheduleListDBFragment.Callbacks, ScheduleInfoDBFragment.Callbacks {

    private var miAdd : MenuItem? = null
    private var miDelete : MenuItem? = null
    private var miChange : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        //val myIntent = intent
        showDBSchedules()

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                logout()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveData()
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        loadData()
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    private fun loadData() {
        ScheduleRepository.getInstance().loadSchedules()
    }

    private fun saveData() {
        ScheduleRepository.getInstance().saveSchedules()
    }

    override fun onStop() {
        saveData()
        super.onStop()
    }

    private fun logout() {
        finish()
    }

    fun checkDelete() {
        val s = ScheduleRepository.getInstance().schedule.value?.discipline
        AlertDialog.Builder(this)
            .setTitle("УДАЛЕНИЕ!")
            .setMessage("Вы действительно хотите удалить расписание $s ?")
            .setPositiveButton("ДА") { _, _ ->
                ScheduleRepository.getInstance().deleteScheduleCurrent()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        miAdd=menu.findItem(R.id.miAdd)
        miDelete=menu.findItem(R.id.miDelete)
        miChange=menu.findItem(R.id.miChange)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miAdd -> {
                //showNewStudent()
                showScheduleDetailDB(Schedule().id)
                true
            }
            R.id.miDelete -> {
                checkDelete()
                true
            }
            R.id.miChange -> {
                //showScheduleInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*fun showNewSchedule() {
        ScheduleRepository.getInstance().newSchedule()
        showScheduleInfo()
    }

    fun showSchedulesList() {
        miAdd?.isVisible=true
        miDelete?.isVisible=true
        miChange?.isVisible=true
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, SchedulesListFragment.getInstance(), SCHEDULE_LIST_FRAGMENT_TAG)
            .commit()
    }

    fun showScheduleInfo() {
        miAdd?.isVisible=false
        miDelete?.isVisible=false
        miChange?.isVisible=false
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, ScheduleInfoFragment.getInstance(), SCHEDULE_INFO_FRAGMENT_TAG)
            //.addToBackStack(null)
            .commit()
    }*/

    override fun onScheduleSelected(scheduleId: UUID) {
        showScheduleDetailDB(scheduleId)
    }

    override fun onScheduleLongClick(scheduleBuilding: String) {
        showToast(scheduleBuilding)
    }

    private fun showScheduleDetailDB(scheduleId: UUID) {
        miAdd?.isVisible=false
        miDelete?.isVisible=false
        miChange?.isVisible=false
        val fragment = ScheduleInfoDBFragment.newInstance(scheduleId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameSchedule, fragment)
            .commit()
    }

    override fun showDBSchedules() {
        miAdd?.isVisible=true
        miDelete?.isVisible=true
        miChange?.isVisible=true
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameSchedule, ScheduleListDBFragment.getInstance(),
                MyConstants.SCHEDULE_LIST_FRAGMENT_TAG
            )
            .commit()
    }

    private fun showToast(scheduleBuilding: String) {
        val text = "Номер корпуса: $scheduleBuilding"
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
        toast.show()
    }
}