package com.example.task35
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnimalFactDao {
    @Query("SELECT * FROM animal_facts WHERE type = :animalType")
    fun getFactsByType(animalType: String): List<AnimalFact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(facts: List<AnimalFact>)
}