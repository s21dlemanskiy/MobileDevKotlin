package com.example.task39.model

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {

    private var baseUrl = "http://10.0.2.2:8181/"
    var retrofit: Retrofit? = null


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