package com.example.task38.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.task38.model.models.Song

@Dao
interface SongsDAO {
    @Query("SELECT * FROM songs")
    fun getSongs(): List<Song>

    @Query("""SELECT 
                * 
               FROM songs 
               WHERE title LIKE '%' || :text || '%' OR
                author LIKE '%' || :text || '%' OR
                 text LIKE '%' || :text || '%'""")
    suspend fun getSongsWithText(text: String): List<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<Song>)

    @Query("DELETE FROM songs")
    suspend fun clear()


    @Transaction
    suspend fun clearAndInsert(songs: List<Song>){
        clear()
        insertAll(songs)
    }
}