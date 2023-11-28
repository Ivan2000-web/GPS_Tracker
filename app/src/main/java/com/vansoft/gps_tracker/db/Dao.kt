package com.vansoft.gps_tracker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insertTrack(trackItem: TrackItem)
    @Query("SELECT * FROM TRACK")
    fun getAllTracks(): Flow<List<TrackItem>>
    @Delete
    suspend fun deleteTrack(trackItem: TrackItem)
}