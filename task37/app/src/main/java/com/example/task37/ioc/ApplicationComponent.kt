package com.example.task37.ioc

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task37.model.AppDatabase
import com.example.task37.model.RetrofitInstance
import com.example.task37.model.SongRetrofitService

class ApplicationComponent(context: Context) {
    private val database = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "songs_db"
    ).build()
    val songsDAO = database.songsDao()
    val api: SongRetrofitService = RetrofitInstance.api
}