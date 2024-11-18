package com.example.educonnect.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true) val id:Long=0,
    val name:String,
    val email:String,
    val image:ByteArray?
)
