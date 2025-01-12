package com.example.task39.model.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song (
    @PrimaryKey
    val id: Int,
    val title: String,
    val text: String,
    val author: String,
    val authorId: Int
)