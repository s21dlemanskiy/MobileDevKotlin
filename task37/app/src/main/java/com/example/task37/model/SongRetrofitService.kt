package com.example.task37.model

import com.example.task37.model.models.RawAuthor
import com.example.task37.model.models.RawSong
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SongRetrofitService {
        @GET("/songs")
        suspend fun getSongs(): Response<List<RawSong>>

        @GET("/authors")
        suspend fun getAuthors(): Response<List<RawAuthor>>

        @GET("/remove/song/{id}")
        suspend fun deleteSong(@Path("id") id: Int): Response<List<RawSong>>

        @GET("/remove/author/{id}")
        suspend fun deleteAuthor(@Path("id") id: Int): Response<List<RawAuthor>>

}