package com.example.task37.utils

import com.example.task37.model.models.RawSong
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.type.TypeReference
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