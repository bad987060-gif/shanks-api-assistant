package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "USER" or "SHANKS"
    val content: String,
    val codeSnippet: String? = null,
    val codeLanguage: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
