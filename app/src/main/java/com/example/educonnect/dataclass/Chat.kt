package com.example.educonnect.dataclass

data class Chat(
    val participants: List<String>,
    val lastMessage: String,
    val timestamp: Long
)
