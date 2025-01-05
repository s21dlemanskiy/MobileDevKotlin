package com.example.task35

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animal_facts")
data class AnimalFact(
    @PrimaryKey val _id: String,
    val text: String,
    val type: String
)