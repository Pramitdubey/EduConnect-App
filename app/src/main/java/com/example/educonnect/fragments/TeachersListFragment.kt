package com.example.educonnect.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.educonnect.R
import com.example.educonnect.adapter.TeacherRecyclerAdapter
import com.example.educonnect.dataclass.Categories
import com.example.educonnect.dataclass.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TeachersListFragment : Fragment(), TeacherRecyclerAdapter.OnItemClickListener {
    private lateinit var teachersRecyclerView: RecyclerView
    private lateinit var teachersRecyclerAdapter: TeacherRecyclerAdapter
    private var teacherList: List<User> = listOf()

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: Categories): TeachersListFragment {
            val fragment = TeachersListFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category.title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teachers_list, container, false)

        teachersRecyclerView = view.findViewById(R.id.ItemsForTeachers)
        teachersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val categoryTitle = arguments?.getString(ARG_CATEGORY)

        if (categoryTitle != null) {
            getTeachersByCategory(categoryTitle) { teachers ->
                teacherList = teachers
                teachersRecyclerAdapter = TeacherRecyclerAdapter(teacherList, this@TeachersListFragment)
                teachersRecyclerView.adapter = teachersRecyclerAdapter
            }
        }

        return view
    }

    private fun getTeachersByCategory(categoryTitle: String?, completion: (List<User>) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("Users")
        val teachersList = mutableListOf<User>()

        database.orderByChild("role").equalTo("Teacher").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (teacherSnapshot in snapshot.children) {
                    val user = teacherSnapshot.getValue(User::class.java)
                    if (user != null) {
                        teachersList.add(user)
                    }
                }

                val filteredTeachers = when (categoryTitle) {
                    "Science" -> teachersList.filter { it.subject == "Science" }
                    "Psychology" -> teachersList.filter { it.subject == "Psychology" }
                    "BioTech" -> teachersList.filter { it.subject == "BioTech" }
                    else -> emptyList()
                }

                completion(filteredTeachers)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error retrieving data: ${error.message}")
                completion(emptyList())
            }
        })
    }

    override fun onItemClick(teacher: User) {
        val newFragment = TeacherProfileFragment.newInstance(teacher)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragment_container, newFragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}

