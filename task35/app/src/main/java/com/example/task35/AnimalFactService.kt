package com.example.task35

import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalFactService {
    @GET("facts")
    suspend fun getAnimalFacts(@Query("animal_type") animalType: String): List<AnimalFact>
}