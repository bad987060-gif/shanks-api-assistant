package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPrefDao {
    @Query("SELECT * FROM user_preferences")
    fun getAllPreferences(): Flow<List<UserPrefEntity>>

    @Query("SELECT * FROM user_preferences WHERE prefKey = :key LIMIT 1")
    suspend fun getPref(key: String): UserPrefEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPref(pref: UserPrefEntity)

    @Query("DELETE FROM user_preferences WHERE prefKey = :key")
    suspend fun deletePref(key: String)
}
