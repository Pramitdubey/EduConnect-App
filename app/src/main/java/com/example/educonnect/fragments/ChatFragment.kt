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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment(), TeacherRecyclerAdapter.OnItemClickListener {

    private lateinit var teacherRecyclerView: RecyclerView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var teacherRecyclerAdapter: TeacherRecyclerAdapter
    private var teacherList: MutableList<User> = mutableListOf()
    private var currentUserUid: String? = null
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)


        mDbRef = FirebaseDatabase.getInstance().getReference()
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid


        teacherRecyclerView = view.findViewById(R.id.teachersChatList)
        teacherRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        sharedViewModel.teachers.observe(viewLifecycleOwner) { teachers ->
            teacherList.clear()
            teacherList.addAll(teachers)
            loadActiveChats()
        }

        return view
    }


    private fun loadActiveChats() {
        currentUserUid?.let { uid ->
            mDbRef.child("chats").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val activeChatTeachers = mutableSetOf<String>() // To avoid duplicates

                    for (chatSnapshot in snapshot.children) {
                        val chatId = chatSnapshot.key
                        if (chatId != null && chatId.contains(uid)) {

                            val otherUid = if (chatId.startsWith(uid)) {
                                chatId.removePrefix(uid)
                            } else {
                                chatId.removeSuffix(uid)
                            }
                            activeChatTeachers.add(otherUid)
                        }
                    }


                    fetchTeacherDetails(activeChatTeachers)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }


    private fun fetchTeacherDetails(activeChatTeachers: Set<String>) {
        mDbRef.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (teacherUid in activeChatTeachers) {
                    val teacherSnapshot = snapshot.child(teacherUid)
                    val teacher = teacherSnapshot.getValue(User::class.java)
                    if (teacher != null && teacher.role == "Teacher" && !teacherList.contains(teacher)) {
                        teacherList.add(teacher)
                    }
                }


                teacherRecyclerAdapter = TeacherRecyclerAdapter(teacherList, this@ChatFragment)
                teacherRecyclerView.adapter = teacherRecyclerAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemClick(teacher: User) {
        val newFragment = StudentSideChatRoom.newInstance(teacher)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, newFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
