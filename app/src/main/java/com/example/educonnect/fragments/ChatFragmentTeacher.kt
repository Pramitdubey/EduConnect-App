package com.example.educonnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.adapter.StudentRecyclerAdapter
import com.example.educonnect.dataclass.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragmentTeacher : Fragment(), StudentRecyclerAdapter.OnItemClickListener {

    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var studentRecyclerAdapter: StudentRecyclerAdapter
    private var studentList: List<User> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_teacher, container, false)

        studentRecyclerView = view.findViewById(R.id.studentsChatList)
        studentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        studentRecyclerAdapter = StudentRecyclerAdapter(studentList, this)
        studentRecyclerView.adapter = studentRecyclerAdapter

        loadStudentData()

        return view
    }

    private fun loadStudentData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val studentList = mutableListOf<User>()

        databaseReference.orderByChild("role").equalTo("Student").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (studentSnapshot in snapshot.children) {
                    val user = studentSnapshot.getValue(User::class.java)
                    if (user != null) {
                        studentList.add(user)
                    }
                }
                studentRecyclerAdapter.updateData(studentList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load students: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(student: User) {
        val newFragment = TeacherSideChatRoom.newInstance(student)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, newFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
