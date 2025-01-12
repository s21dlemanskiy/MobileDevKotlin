package com.example.task38.model
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.task38.model.models.Song

@Database(entities = [Song::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDAO
}