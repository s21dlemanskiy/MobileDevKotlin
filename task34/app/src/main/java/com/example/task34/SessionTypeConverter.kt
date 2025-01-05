package com.example.task34

import androidx.room.TypeConverter

class SessionTypeConverter {
    @TypeConverter
    fun fromSessionType(sessionType: SessionType): String {
        return sessionType.name
    }

    @TypeConverter
    fun toSessionType(sessionType: String): SessionType {
        return SessionType.valueOf(sessionType)
    }
}