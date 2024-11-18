package com.example.educonnect

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.educonnect.fragments.ChatFragment
import com.example.educonnect.fragments.ProfileFragment
import com.example.educonnect.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        val bottomNavigationView:BottomNavigationView=findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.navigation_chat->{
                    replaceFragment(ChatFragment())
                    true
                }
                R.id.navigation_search->{
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.navigation_profile->{
                    replaceFragment(ProfileFragment())
                    true
                }
                else->false
            }
        }

        replaceFragment(ChatFragment())
    }

    private fun replaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
    }
}