package com.example.educonnect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.educonnect.dataclass.User

class SharedViewModel : ViewModel() {
    private val _teachers = MutableLiveData<List<User>>(emptyList())
    val teachers: LiveData<List<User>> = _teachers

    fun addTeacher(teacher: User) {
        val updatedList = _teachers.value.orEmpty().toMutableList()
        updatedList.add(teacher)
        _teachers.value = updatedList
    }
}
