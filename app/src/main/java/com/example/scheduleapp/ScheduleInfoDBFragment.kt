package com.example.scheduleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.scheduleapp.MyConstants.GROUP_ID_TAG
import com.example.scheduleapp.MyConstants.SCHEDULE_ID_TAG
import com.example.scheduleapp.data.GroupSC
import com.example.scheduleapp.data.Schedule
import com.example.scheduleapp.repository.ScheduleDBRepository
import java.util.*

class ScheduleInfoDBFragment : Fragment() {
    private var schedule : Schedule? = null
    private lateinit var scheduleInfoViewModel : ScheduleInfoDBViewModel
    private lateinit var scheduleListFragment : ScheduleListDBFragment

    private lateinit var tvDiscipline : TextView
    private lateinit var tvTeacherName : TextView
    private lateinit var tvBuilding : TextView
    private lateinit var tvPosition : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvDuration : TextView
    private lateinit var tvClassroom : TextView
    private lateinit var tvCoupleNumber : TextView

    private lateinit var etDiscipline : EditText
    private lateinit var etTeacherName : EditText
    private lateinit var etBuilding : EditText
    private lateinit var etPosition : EditText
    private lateinit var dpDate : DatePicker
    private lateinit var etTime : EditText
    private lateinit var etDuration : EditText
    private lateinit var etClassroom : EditText
    private lateinit var etCoupleNumber : EditText

    private lateinit var rgType : RadioGroup
    private lateinit var rbLecture : RadioButton
    private lateinit var rbPractice : RadioButton

    private lateinit var btnSave : Button
    private lateinit var btnDelete : Button
    private lateinit var btnCancel : Button

    companion object {
        fun newInstance(scheduleID: UUID): ScheduleInfoDBFragment {
            val args = Bundle().apply {
                putString(SCHEDULE_ID_TAG, scheduleID.toString())
            }
            return ScheduleInfoDBFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scheduleId: UUID = UUID.fromString(arguments?.getString(SCHEDULE_ID_TAG))
        scheduleInfoViewModel = ViewModelProvider(this).get(ScheduleInfoDBViewModel::class.java)
        scheduleInfoViewModel.loadSchedule(scheduleId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.schedule_info, container, false)

        etDiscipline=view.findViewById(R.id.discipline)
        etTeacherName=view.findViewById(R.id.teacherName)
        etBuilding=view.findViewById(R.id.building)
        etPosition=view.findViewById(R.id.position)
        etDuration=view.findViewById(R.id.duration)
        etClassroom=view.findViewById(R.id.classroom)
        etTime=view.findViewById(R.id.time)
        dpDate=view.findViewById(R.id.datePicker)
        etCoupleNumber=view.findViewById(R.id.coupleNumber)
        rgType=view.findViewById(R.id.rgType)
        rbLecture=view.findViewById(R.id.rbLecture)
        rbPractice=view.findViewById(R.id.rbPractice)
        btnSave=view.findViewById(R.id.btOk)
        btnSave.setOnClickListener {
            if (schedule == null) {
                schedule = Schedule(groupID = ScheduleDBRepository.get().groupID)
                updateSchedule()
                scheduleInfoViewModel.newSchedule(schedule!!)
            } else {
                updateSchedule()
                scheduleInfoViewModel.saveSchedule(schedule!!)
            }
            //TODO("выход")
            callbacks?.showDBSchedules()
        }
        btnDelete=view.findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener {
            if (schedule != null) {
                scheduleInfoViewModel.dropSchedule(schedule!!)
            }
            callbacks?.showDBSchedules()
        }
        btnCancel=view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            // TODO("выход")
            callbacks?.showDBSchedules()
        }
        return view
    }

    private fun updateSchedule() {
        val date = GregorianCalendar(dpDate.year, dpDate.month, dpDate.dayOfMonth)
        schedule?.discipline = etDiscipline.text.toString()
        schedule?.teacherName = etTeacherName.text.toString()
        schedule?.building = etBuilding.text.toString().toInt()
        schedule?.position = etPosition.text.toString()
        schedule?.date = date.time
        schedule?.date = Date(dpDate.year - 1900, dpDate.month, dpDate.dayOfMonth, etTime.text.toString().split(":")[0].toInt(), etTime.text.toString().split(":")[1].toInt())
        schedule?.duration = etDuration.text.toString().toInt()
        schedule?.classroom = etClassroom.text.toString()
        schedule?.coupleNumber = etCoupleNumber.text.toString().toInt()
        schedule?.type = if (rbLecture.isChecked) rbLecture.text.toString() else rbPractice.text.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scheduleInfoViewModel.scheduleLiveData.observe(
            viewLifecycleOwner
        ) { schedule ->
            schedule?.let {
                this.schedule = schedule
                updateUI()
            }
        }
    }

    fun updateUI() {
        etDiscipline.setText(schedule?.discipline)
        etTeacherName.setText(schedule?.teacherName)
        etBuilding.setText(schedule?.building.toString())
        etPosition.setText(schedule?.position)
        val date = GregorianCalendar()
        date.time = schedule?.date
        dpDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        etTime.setText("${date.get(Calendar.HOUR)}:${date.get(Calendar.MINUTE)}")
        etDuration.setText(schedule?.duration.toString())
        etClassroom.setText(schedule?.classroom)
        etCoupleNumber.setText(schedule?.coupleNumber.toString())
        rgType.check(if(schedule?.type == "Лекция") R.id.rbLecture else R.id.rbPractice)
    }

    interface Callbacks {
        fun showDBSchedules()
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}