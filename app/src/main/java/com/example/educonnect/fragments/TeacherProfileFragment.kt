package com.example.educonnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.educonnect.R
import com.example.educonnect.SharedViewModel
import com.example.educonnect.dataclass.Categories
import com.example.educonnect.dataclass.User

class TeacherProfileFragment : Fragment() {
    private lateinit var etName:TextView
    private lateinit var etSubject:TextView
    private lateinit var etEmail:TextView
    private lateinit var btnAdd: Button
    private lateinit var sharedViewModel: SharedViewModel


    companion object {
        private const val ARG_TEACHER_NAME = "teacher_name"
        private const val ARG_TEACHER_EMAIL = "teacher_email"
        private const val ARG_TEACHER_SUBJECT = "teacher_subject"
        private const val ARG_TEACHER_ROLE = "teacher_role"
        private const val ARG_TEACHER_UID = "teacher_uid"

        fun newInstance(teacher: User): TeacherProfileFragment {
            val fragment = TeacherProfileFragment()
            val args = Bundle()
            args.putString(ARG_TEACHER_NAME, teacher.name)
            args.putString(ARG_TEACHER_EMAIL,teacher.email)
            args.putString(ARG_TEACHER_SUBJECT,teacher.subject)
            args.putString(ARG_TEACHER_ROLE,teacher.role)
            args.putString(ARG_TEACHER_UID,teacher.uid)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_teacher_profile, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        etName=view.findViewById(R.id.tv_teacher_name)
        etSubject=view.findViewById(R.id.tv_teacher_subject)
        etEmail=view.findViewById(R.id.tv_teacher_email)
        btnAdd=view.findViewById(R.id.btn_add_teacher)

        val name=arguments?.getString(ARG_TEACHER_NAME)
        val email=arguments?.getString(ARG_TEACHER_EMAIL)
        val subject=arguments?.getString(ARG_TEACHER_SUBJECT)
        val role=arguments?.getString(ARG_TEACHER_NAME)
        val uid=arguments?.getString(ARG_TEACHER_NAME)

        etName.text=name
        etSubject.text=subject
        etEmail.text=email

        btnAdd.setOnClickListener{
            val teacher = User(name,email,role,subject,uid)
            sharedViewModel.addTeacher(teacher)
            Toast.makeText(context, "Teacher added to chat", Toast.LENGTH_SHORT).show()

        }
        return view
    }


}