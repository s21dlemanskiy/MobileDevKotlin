package com.example.task38.ioc

import android.content.Context
import androidx.room.Room
import com.example.task38.model.AppDatabase
import com.example.task38.model.RetrofitInstance
import com.example.task38.model.SongRetrofitService

class ApplicationComponent(context: Context) {
    private val database = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "songs_db"
    ).build()
    val songsDAO = database.songsDao()
    val api: SongRetrofitService = RetrofitInstance.api
}