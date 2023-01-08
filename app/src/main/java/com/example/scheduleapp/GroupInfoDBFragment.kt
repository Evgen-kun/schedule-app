package com.example.scheduleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.scheduleapp.data.Group
import java.util.*

class GroupInfoDBFragment : Fragment() {
    private var group : Group? = null
    private lateinit var groupInfoViewModel : GroupInfoDBViewModel
    private lateinit var groupListFragment : GroupListDBFragment

    private lateinit var etName : EditText
    private lateinit var btnGrSave : Button
    private lateinit var btnGrDelete : Button
    private lateinit var btnGrCancel : Button

    companion object {
        fun newInstance(groupID: UUID): GroupInfoDBFragment {
            val args = Bundle().apply {
                putString(MyConstants.SCHEDULE_ID_TAG, groupID.toString())
            }
            return GroupInfoDBFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val groupId: UUID = UUID.fromString(arguments?.getString(MyConstants.GROUP_ID_TAG))
        groupInfoViewModel = ViewModelProvider(this).get(GroupInfoDBViewModel::class.java)
        groupInfoViewModel.loadGroup(groupId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.group_info, container, false)


        etName=view.findViewById(R.id.grName)
        btnGrSave=view.findViewById(R.id.btGrOk)
        btnGrSave.setOnClickListener {
            if (group == null) {
                group = Group()
                updateGroup()
                groupInfoViewModel.newGroup(group!!)
            } else {
                updateGroup()
                groupInfoViewModel.saveGroup(group!!)
            }
            //TODO("выход")
            callbacks?.showDBGroups()
        }
        btnGrDelete=view.findViewById(R.id.btnGrDelete)
        btnGrDelete.setOnClickListener {
            if (group != null) {
                groupInfoViewModel.dropGroup(group!!)
            }
            callbacks?.showDBGroups()
        }
        btnGrCancel=view.findViewById(R.id.btnGrCancel)
        btnGrCancel.setOnClickListener {
            // TODO("выход")
            callbacks?.showDBGroups()
        }
        return view
    }

    private fun updateGroup() {
        group?.name = etName.text.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupInfoViewModel.groupLiveData.observe(
            viewLifecycleOwner
        ) { group ->
            group?.let {
                this.group = group
                updateUI()
            }
        }
    }

    fun updateUI() {
        etName.setText(group?.name)
    }

    interface Callbacks {
        fun showDBGroups()
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