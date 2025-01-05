package com.example.task35

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://cat-fact.herokuapp.com/"

    // Create a custom Gson instance
    private val gson: Gson = GsonBuilder()
        .setLenient() // Allows lenient parsing of JSON
        .create()

    val api: AnimalFactService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AnimalFactService::class.java)
    }
}