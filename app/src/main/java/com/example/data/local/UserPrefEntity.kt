package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPrefEntity(
    @PrimaryKey val prefKey: String,
    val prefValue: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
