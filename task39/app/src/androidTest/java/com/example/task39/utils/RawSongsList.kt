package com.example.task39.utils

import com.example.task39.model.models.RawSong
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = RawSongsListDeserializer::class)
data class RawSongsList(
    val songs: List<RawSong>
)

private class RawSongsListDeserializer : JsonDeserializer<RawSongsList>() {
    override fun deserialize(
        p: com.fasterxml.jackson.core.JsonParser?,
        ctxt: DeserializationContext?
    ): RawSongsList {
        val type = ctxt!!.typeFactory.constructCollectionType(List::class.java, RawSong::class.java)
        val songs: List<RawSong> = ctxt.readValue(p, type)
        return RawSongsList(songs)
    }
}