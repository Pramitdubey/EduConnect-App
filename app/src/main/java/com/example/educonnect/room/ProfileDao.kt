package com.example.educonnect.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Insert
    suspend fun insert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Query("SELECT * FROM profile_table WHERE id = :id")
    suspend fun getProfileById(id: Long): Profile?
}