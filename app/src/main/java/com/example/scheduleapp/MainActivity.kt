package com.example.scheduleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.example.scheduleapp.MyConstants.GROUP_LIST_FRAGMENT_TAG
import com.example.scheduleapp.MyConstants.SCHEDULE_INFO_FRAGMENT_TAG
import com.example.scheduleapp.MyConstants.SCHEDULE_LIST_FRAGMENT_TAG
import com.example.scheduleapp.data.Group
import com.example.scheduleapp.data.Schedule
import com.example.scheduleapp.repository.ScheduleDBRepository
import com.example.scheduleapp.repository.ScheduleRepository
import java.util.*

class MainActivity : AppCompatActivity(), GroupListDBFragment.Callbacks, GroupInfoDBFragment.Callbacks {

    private var miGrAdd : MenuItem? = null
    private var miGrDelete : MenuItem? = null
    private var miGrChange : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showDBGroups()

        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                checkLogout()
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //saveData()
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        //loadData()
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    /*private fun loadData() {
        GroupRepository.getInstance().loadGroups()
    }

    private fun saveData() {
        GroupRepository.getInstance().saveGroups()
    }*/

    override fun onStop() {
        //saveData()
        super.onStop()
    }

    private fun checkLogout() {
        AlertDialog.Builder(this)
            .setTitle("Выход!")
            .setMessage("Вы действительно хотите выйти из приложения?")

            .setPositiveButton("ДА") { _, _ ->
                finish()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    /*fun checkDelete() {
        val s = GroupRepository.getInstance().group.value?.name
        AlertDialog.Builder(this)
            .setTitle("УДАЛЕНИЕ!")
            .setMessage("Вы действительно хотите удалить группу $s ?")
            .setPositiveButton("ДА") { _, _ ->
                GroupRepository.getInstance().deleteGroupCurrent()
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.group_menu, menu)
        miGrAdd=menu.findItem(R.id.miGrAdd)
        miGrDelete=menu.findItem(R.id.miGrDelete)
        miGrChange=menu.findItem(R.id.miGrChange)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miGrAdd -> {
                //showNewGroup()
                showGroupDetailDB(Group().id)
                true
            }
            R.id.miGrDelete -> {
                //checkDelete()
                true
            }
            R.id.miGrChange -> {
                //showGroupInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onGroupSelected(groupId: UUID) {
        showGroupDetailDB(groupId)
    }

    private fun showGroupDetailDB(groupId: UUID) {
        miGrAdd?.isVisible=false
        miGrDelete?.isVisible=false
        miGrChange?.isVisible=false
        val fragment = GroupInfoDBFragment.newInstance(groupId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain, fragment)
            .commit()
    }

    override fun showDBGroups() {
        miGrAdd?.isVisible=true
        miGrDelete?.isVisible=true
        miGrChange?.isVisible=true
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameMain, GroupListDBFragment.getInstance(), GROUP_LIST_FRAGMENT_TAG)
            .commit()
    }

}