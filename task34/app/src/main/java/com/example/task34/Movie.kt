package com.example.task34
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "movies")
@TypeConverters(SessionTypeConverter::class)
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val time: String,
    val cinema: String,
    val film: String,
    val hallNumber: Int,
    val sessionType: SessionType
)
