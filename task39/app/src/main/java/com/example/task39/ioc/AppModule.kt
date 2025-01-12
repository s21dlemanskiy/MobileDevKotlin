package com.example.task39.ioc

import com.example.task39.model.RetrofitInstance
import com.example.task39.model.SongRetrofitService
import com.example.task39.model.SongsDAO
import com.example.task39.model.SongsRepository
import com.example.task39.model.SongsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideSongsRepository(songRetrofitService: SongRetrofitService, songsDAO: SongsDAO): SongsRepository {
        return SongsRepositoryImpl(songRetrofitService, songsDAO)
    }
}