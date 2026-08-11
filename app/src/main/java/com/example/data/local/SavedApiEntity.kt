package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_apis")
data class SavedApiEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val method: String,
    val url: String,
    val headersJson: String,
    val bodyJson: String,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
