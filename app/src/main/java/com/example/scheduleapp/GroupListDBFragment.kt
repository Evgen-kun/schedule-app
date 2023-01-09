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
import com.example.scheduleapp.data.GroupSC
import java.util.*

class GroupListDBFragment : Fragment() {
    private lateinit var groupsListViewModel: GroupListDBViewModel
    private lateinit var groupsListRecyclerView: RecyclerView

    private var adapter: GroupListAdapter? = GroupListAdapter(emptyList())

    companion object {
        private var INSTANCE : GroupListDBFragment? = null

        fun getInstance(): GroupListDBFragment {
            if (INSTANCE == null) {
                INSTANCE = GroupListDBFragment()
            }
            return INSTANCE?: throw IllegalStateException("Отображение списка не создано!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.list_of_groups, container, false)
        groupsListRecyclerView = layoutView.findViewById(R.id.rvGrList)
        groupsListRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        groupsListRecyclerView.adapter = adapter
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupsListViewModel = ViewModelProvider(this).get(GroupListDBViewModel::class.java)
        groupsListViewModel.groupListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { groups ->
                groups?.let {
                    updateUI(groups)
                }
            })
    }

    private inner class GroupListAdapter(private val items: List<GroupSC>)
        : RecyclerView.Adapter<GroupHolder>() {
        override fun onCreateViewHolder(
            parrent : ViewGroup,
            viewType :Int
        ):GroupHolder{
            val view = layoutInflater.inflate(R.layout.groups_list_element, parrent, false)
            return GroupHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: GroupHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    private inner class GroupHolder(view : View)
        : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener{
        private lateinit var group: GroupSC
        private val nameTextView : TextView = itemView.findViewById(R.id.tvName)
        private val clLayout: ConstraintLayout = itemView.findViewById(R.id.clGrCL)

        fun bind(group: GroupSC){
            //Log.d(MyConstants.TAG, "bind 1 $schedule")
            this.group = group
            nameTextView.text = group.name
            //Log.d(MyConstants.TAG, "bind 2 $schedule")
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            callbacks?.onGroupSelected(group.id)
        }

        override fun onLongClick(v: View?): Boolean {
            callbacks?.onGroupLongClick(group.id)
            return true
        }

    }

    private fun updateUI(groups: List<GroupSC>) {
        if (groups==null) return
        adapter=GroupListAdapter(groups)
        groupsListRecyclerView.adapter = adapter
    }

    interface Callbacks {
        fun onGroupSelected(groupId: UUID)
        fun onGroupLongClick(groupId: UUID)
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