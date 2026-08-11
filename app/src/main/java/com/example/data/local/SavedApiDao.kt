package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedApiDao {
    @Query("SELECT * FROM saved_apis ORDER BY timestamp DESC")
    fun getAllSavedApis(): Flow<List<SavedApiEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApi(api: SavedApiEntity): Long

    @Delete
    suspend fun deleteApi(api: SavedApiEntity)

    @Query("DELETE FROM saved_apis WHERE id = :id")
    suspend fun deleteApiById(id: Int)
}
