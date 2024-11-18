package com.example.educonnect.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Profile::class], version=1)
abstract class ProfileDatabase: RoomDatabase() {
    abstract fun profileDao():ProfileDao

    companion object{
        @Volatile
        private var INSTANCE:ProfileDatabase?=null
        fun getDatabase(context: Context):ProfileDatabase{
            return INSTANCE?: synchronized(this){

                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    ProfileDatabase::class.java,
                    "profile_database").build()

                INSTANCE=instance
                instance

            }

        }
    }
}