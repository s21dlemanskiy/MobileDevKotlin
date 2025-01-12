package com.example.task37.model

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {

    private var baseUrl = "http://10.0.2.2:8181/"
    var retrofit: Retrofit? = null

    // Create a custom Gson instance
//    private val gson: Gson = GsonBuilder()
//        .setLenient() // Allows lenient parsing of JSON
//        .create()
    private val jackson = JacksonConverterFactory.create()

    fun updateBaseUrl(newUrl: String) {
        baseUrl = newUrl
        retrofit = null // Reset retrofit instance
    }

    val api: SongRetrofitService
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(jackson)
                    .build()
            }
            return retrofit!!.create(SongRetrofitService::class.java)
        }
}