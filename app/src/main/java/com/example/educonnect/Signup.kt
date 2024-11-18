package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.educonnect.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.log

class Signup : AppCompatActivity() {

    private lateinit var etName:EditText
    private lateinit var etEmail:EditText
    private lateinit var etPassword:EditText
    private lateinit var roleSpinner:Spinner
    private lateinit var subjectSpinner:Spinner
    private lateinit var btnSignin:Button
    private lateinit var login:TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etName=findViewById(R.id.editTextName)
        etEmail=findViewById(R.id.editTextEmail)
        etPassword=findViewById(R.id.editTextPassword)
        subjectSpinner=findViewById(R.id.spinnerSubject)
        roleSpinner=findViewById(R.id.spinnerRole)
        btnSignin=findViewById(R.id.signinSubmit)
        login=findViewById(R.id.textViewLogin)

        auth = FirebaseAuth.getInstance()

        login.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

        }

        val roles = arrayOf("Teacher", "Student")
        val roleAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = roleAdapter

        val subjects = arrayOf("Science", "Psychology", "BioTech")
        val subjectAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subjectSpinner.adapter = subjectAdapter

        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                subjectSpinner.visibility = if (roles[position] == "Teacher") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }




        btnSignin.setOnClickListener{
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val role = roleSpinner.selectedItem.toString()
            val subject = if (role == "Teacher") subjectSpinner.selectedItem.toString() else null
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signin(email,password,name,role,subject)
        }

    }

    private fun signin(
        email: String,
        password: String,
        name: String,
        role: String,
        subject: String?
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val userId = auth.currentUser?.uid
                    val userInfo = User(name, email, role, subject,userId)


                    userId?.let {
                        FirebaseDatabase.getInstance().getReference("Users").child(it).setValue(userInfo)
                            .addOnSuccessListener {
                                if(role == "Student"){
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    val intent = Intent(this, MainActivityForTeacher::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to store user info: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Sign In Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }


}



