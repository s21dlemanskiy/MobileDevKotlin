package com.example.task39.ioc

import com.example.task39.model.RetrofitInstance
import com.example.task39.model.SongRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofitInstance(): RetrofitInstance {
        return RetrofitInstance
    }

    @Provides
    fun provideSongRetrofitService(retrofitInstance: RetrofitInstance): SongRetrofitService {
        return retrofitInstance.api
    }
}