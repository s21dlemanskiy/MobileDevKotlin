package com.example.task39.ioc

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task39.App
import com.example.task39.model.AppDatabase
import com.example.task39.model.RetrofitInstance
import com.example.task39.model.SongRetrofitService
import com.example.task39.model.SongsDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java,
            name = "songs_db"
        ).build()
    }

    @Provides
    fun provideSongRetrofitService(database: AppDatabase): SongsDAO {
        return database.songsDao()
    }
}