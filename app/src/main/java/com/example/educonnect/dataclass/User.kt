package com.example.educonnect.dataclass

data class User(val name: String?=null,
                val email: String?=null,
                val role: String?=null,
                val subject: String?=null,
                val experience:String?=null,
                val latitude: Double? = null,
                val longitude: Double? = null,
                val uid:String?=null)
