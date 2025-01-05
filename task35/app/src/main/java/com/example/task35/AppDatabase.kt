package com.example.task35
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AnimalFact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animalFactDao(): AnimalFactDao
}