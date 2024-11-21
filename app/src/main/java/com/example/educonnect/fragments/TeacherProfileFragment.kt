package com.example.educonnect.fragments

import android.content.Intent
import android.net.Uri
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TeacherProfileFragment : Fragment() {
    private lateinit var etName:TextView
    private lateinit var etSubject:TextView
    private lateinit var etEmail:TextView
    private lateinit var btnAdd: Button
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var etExperience:TextView
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private lateinit var sharedViewModel: SharedViewModel

    companion object {
        private const val ARG_TEACHER_NAME = "teacher_name"
        private const val ARG_TEACHER_EMAIL = "teacher_email"
        private const val ARG_TEACHER_SUBJECT = "teacher_subject"
        private const val ARG_TEACHER_ROLE = "teacher_role"
        private const val ARG_TEACHER_EXPERIENCE = "teacher_experience"
        private const val ARG_TEACHER_LATITUDE = "latitude"
        private const val ARG_TEACHER_LONGITUDE = "longitude"
        private const val ARG_TEACHER_UID = "teacher_uid"

        fun newInstance(teacher: User): TeacherProfileFragment {
            val fragment = TeacherProfileFragment()
            val args = Bundle()
            args.putString(ARG_TEACHER_NAME, teacher.name)
            args.putString(ARG_TEACHER_EMAIL,teacher.email)
            args.putString(ARG_TEACHER_SUBJECT,teacher.subject)
            args.putString(ARG_TEACHER_ROLE,teacher.role)
            args.putString(ARG_TEACHER_EXPERIENCE,teacher.experience)
            args.putString(ARG_TEACHER_LATITUDE, teacher.latitude.toString())
            args.putString(ARG_TEACHER_LONGITUDE, teacher.longitude.toString())
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
        etExperience=view.findViewById(R.id.tv_teacher_experience)
        floatingActionButton=view.findViewById(R.id.teacher_profile_fab)

        val name=arguments?.getString(ARG_TEACHER_NAME)
        val email=arguments?.getString(ARG_TEACHER_EMAIL)
        val subject=arguments?.getString(ARG_TEACHER_SUBJECT)
        val role=arguments?.getString(ARG_TEACHER_ROLE)
        val uid=arguments?.getString(ARG_TEACHER_UID)
        val experience=arguments?.getString(ARG_TEACHER_EXPERIENCE)
        latitude=arguments?.getString(ARG_TEACHER_LATITUDE)?.toDoubleOrNull() ?: 0.0
        longitude=arguments?.getString(ARG_TEACHER_LONGITUDE)?.toDoubleOrNull() ?: 0.0

        etName.text=name
        etSubject.text=subject
        etEmail.text=email
        etExperience.text=experience


        btnAdd.setOnClickListener{
            val teacher = User(name,email,role,subject,experience,
                latitude,longitude,uid)
            sharedViewModel.addTeacher(teacher)
            Toast.makeText(context, "Teacher added to chat", Toast.LENGTH_SHORT).show()

        }

        floatingActionButton.setOnClickListener {
            Toast.makeText(context, "Redirecting To Teachers Current Location...", Toast.LENGTH_SHORT).show()
            openMap()
        }

        return view
    }

    private fun openMap() {
        val uri = Uri.parse("geo: ${latitude},${longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW,uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }


}