package com.example.educonnect

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.educonnect.fragments.ChatFragment
import com.example.educonnect.fragments.ChatFragmentTeacher
import com.example.educonnect.fragments.ProfileFragment
import com.example.educonnect.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityForTeacher : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_for_teacher)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView: BottomNavigationView =findViewById(R.id.bottom_navigation_teacher)

        bottomNavigationView.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.navigation_chat_teacher->{
                    replaceFragment(ChatFragmentTeacher())
                    true
                }
                R.id.navigation_profile_teacher->{
                    replaceFragment(ProfileFragment())
                    true
                }
                else->false
            }
        }

        replaceFragment(ChatFragmentTeacher())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
    }
}