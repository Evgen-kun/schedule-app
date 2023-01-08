package com.example.scheduleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.data.Schedule
import java.util.*

class ScheduleListDBFragment : Fragment() {

    private lateinit var schedulesListViewModel: ScheduleListDBViewModel
    private lateinit var schedulesListRecyclerView: RecyclerView

    private var adapter: ScheduleListAdapter? = ScheduleListAdapter(emptyList())

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
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        schedulesListViewModel = ViewModelProvider(this).get(ScheduleListDBViewModel::class.java)
        schedulesListViewModel.scheduleListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { schedules ->
                schedules?.let {
                    updateUI(schedules)
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
        private val clLayout: ConstraintLayout = itemView.findViewById(R.id.clCL)

        fun bind(schedule: Schedule){
            //Log.d(MyConstants.TAG, "bind 1 $schedule")
            this.schedule = schedule
            fioTextView.text = schedule.discipline
            dayTextView.text = schedule.day
            timeTextView.text = schedule.time
            //Log.d(MyConstants.TAG, "bind 2 $schedule")
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            callbacks?.onScheduleSelected(schedule.id)
        }

        override fun onLongClick(v: View?): Boolean {
            callbacks?.onScheduleLongClick(schedule.building.toString())
            return true
        }

    }

    private fun updateUI(schedules: List<Schedule>) {
        if (schedules==null) return
        adapter=ScheduleListAdapter(schedules)
        schedulesListRecyclerView.adapter = adapter
    }

    interface Callbacks {
        fun onScheduleSelected(scheduleId: UUID)
        fun onScheduleLongClick(scheduleBuilding: String)
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