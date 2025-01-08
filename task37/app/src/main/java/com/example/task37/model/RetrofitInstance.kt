package com.example.task37.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://10.0.2.2:8181/"

    // Create a custom Gson instance
    private val gson: Gson = GsonBuilder()
        .setLenient() // Allows lenient parsing of JSON
        .create()

    val api: SongRetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SongRetrofitService::class.java)
    }
}