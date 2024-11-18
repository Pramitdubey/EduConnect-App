package com.example.educonnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.SharedViewModel
import com.example.educonnect.adapter.TeacherRecyclerAdapter
import com.example.educonnect.dataclass.User

class ChatFragment : Fragment(),TeacherRecyclerAdapter.OnItemClickListener {

    private lateinit var teacherRecylerView:RecyclerView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var teacherRecyclerAdapter: TeacherRecyclerAdapter
    private var teacherList: List<User> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_chat, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        teacherRecylerView=view.findViewById(R.id.teachersChatList)
        teacherRecylerView.layoutManager = LinearLayoutManager(requireContext())

        sharedViewModel.teachers.observe(viewLifecycleOwner) { teachers ->
            teacherList = teachers
            teacherRecyclerAdapter = TeacherRecyclerAdapter(teacherList, this@ChatFragment)
            teacherRecylerView.adapter = teacherRecyclerAdapter
        }

        return view

    }

    override fun onItemClick(teacher: User) {
        val newFragment = StudentSideChatRoom.newInstance(teacher)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, newFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }


}