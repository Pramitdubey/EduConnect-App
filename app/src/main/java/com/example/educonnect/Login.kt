package com.example.educonnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.educonnect.dataclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var signUp:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etEmail = findViewById(R.id.editTextEmail)
        etPassword = findViewById(R.id.editTextPassword)
        btnLogin = findViewById(R.id.loginSubmit)
        signUp=findViewById(R.id.textViewRegister)
        auth = FirebaseAuth.getInstance()

        signUp.setOnClickListener{
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)

        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if(email.isEmpty() ){
                etEmail.setError("Cannot be Empty")
                etEmail.text.clear()
            }
            else if (password.isEmpty()){
                etPassword.setError("Cannot be Empty")
                etPassword.text.clear()
            }
            else{
                login(email,password)
            }

        }


    }
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                    val uid = it.uid
                    val databaseReference = FirebaseDatabase.getInstance().getReference("Users/$uid")

                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val userInfo = dataSnapshot.getValue(User::class.java)
                            if (userInfo != null) {
                                val intent = if (userInfo.role == "Teacher") {
                                    Intent(this@Login, MainActivityForTeacher::class.java)
                                } else {
                                    Intent(this@Login, MainActivity::class.java)
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, "User information not found.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(applicationContext, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

}