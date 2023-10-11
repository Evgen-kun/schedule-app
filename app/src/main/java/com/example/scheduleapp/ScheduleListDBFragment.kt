package com.example.scheduleapp

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.data.Schedule
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.*

class ScheduleListDBFragment : Fragment() {

    private lateinit var schedulesListViewModel: ScheduleListDBViewModel
    private lateinit var schedulesListRecyclerView: RecyclerView
    private lateinit var tabLayoutDays: TabLayout

    private var adapter: ScheduleListAdapter? = ScheduleListAdapter(emptyList())
    private var selectedDay: String = ""

    companion object {
        private var INSTANCE : ScheduleListDBFragment? = null

        fun getInstance(): ScheduleListDBFragment {
            if (INSTANCE == null) {
                INSTANCE = ScheduleListDBFragment()
            }
            return INSTANCE?: throw IllegalStateException("Отображение списка не создано!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.list_of_schedules, container, false)
        schedulesListRecyclerView = layoutView.findViewById(R.id.rvList)
        schedulesListRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        schedulesListRecyclerView.adapter = adapter

        tabLayoutDays = layoutView.findViewById(R.id.tlDays)
        tabLayoutDays.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedDay = when (tab?.position) {
                    0 -> "Понедельник"
                    1 -> "Вторник"
                    2 -> "Среда"
                    3 -> "Четверг"
                    4 -> "Пятница"
                    5 -> "Суббота"
                    6 -> "Воскресенье"
                    else -> "Не определено"
                }
                updateSchedulesListViewModel()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateSchedulesListViewModel()
    }

    private fun updateSchedulesListViewModel() {
        schedulesListViewModel = ViewModelProvider(this).get(ScheduleListDBViewModel::class.java)
        schedulesListViewModel.scheduleListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { schedules ->
                schedules?.let {
                    updateUI(schedules.filter { it.day == selectedDay })
                }
            })
    }

    private inner class ScheduleListAdapter(private val items: List<Schedule>)
        : RecyclerView.Adapter<ScheduleHolder>() {
        override fun onCreateViewHolder(
            parrent : ViewGroup,
            viewType :Int
        ):ScheduleHolder{
            val view = layoutInflater.inflate(R.layout.schedules_list_element, parrent, false)
            return ScheduleHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ScheduleHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    private inner class ScheduleHolder(view : View)
        : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener{
        private lateinit var schedule: Schedule
        private val fioTextView : TextView = itemView.findViewById(R.id.tvFIO)
        private val dayTextView : TextView = itemView.findViewById(R.id.tvDayOfMonth)
        private val timeTextView : TextView = itemView.findViewById(R.id.tvTime)
        private val classroomTextView : TextView = itemView.findViewById(R.id.tvElClassroom)
        private val nameTextView : TextView = itemView.findViewById(R.id.tvElName)
        private val coupleNumberTextView : TextView = itemView.findViewById(R.id.tvCoupleNumber)
        private val typeTextView : TextView = itemView.findViewById(R.id.tvType)
        private val typeFrameLayout : FrameLayout = itemView.findViewById(R.id.flType)
        private val clLayout: ConstraintLayout = itemView.findViewById(R.id.clCL)

        fun bind(schedule: Schedule){
            //Log.d(MyConstants.TAG, "bind 1 $schedule")
            this.schedule = schedule
            fioTextView.text = schedule.discipline
            dayTextView.text = schedule.day
            timeTextView.text = schedule.time
            classroomTextView.text = schedule.classroom
            nameTextView.text = schedule.teacherName
            coupleNumberTextView.text = schedule.coupleNumber.toString()
            val drawable = resources.getDrawable(R.drawable.circle)
            drawable.setColorFilter(
                if (schedule.type == "Лекция") resources.getColor(R.color.lecture_color)
                else resources.getColor(R.color.practice_color),
                PorterDuff.Mode.SRC_IN
            )
            coupleNumberTextView.background = drawable
            typeTextView.text = schedule.type
            typeFrameLayout.setBackgroundColor(
                if (schedule.type == "Лекция") resources.getColor(R.color.lecture_color)
                else resources.getColor(R.color.practice_color)
            )
            //Log.d(MyConstants.TAG, "bind 2 $schedule")
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            callbacks?.onScheduleSelected(schedule.building.toString())
        }

        override fun onLongClick(v: View?): Boolean {
            callbacks?.onScheduleLongClick(schedule.id)
            return true
        }

    }

    private fun updateUI(schedules: List<Schedule>) {
        if (schedules==null) return
        adapter=ScheduleListAdapter(schedules)
        schedulesListRecyclerView.adapter = adapter
    }

    interface Callbacks {
        fun onScheduleLongClick(scheduleId: UUID)
        fun onScheduleSelected(scheduleBuilding: String)
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