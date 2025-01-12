package com.example.task37.model.models

import com.fasterxml.jackson.annotation.JsonProperty

data class RawSong(
    @JsonProperty("id") val id: Int,
    @JsonProperty("title") val title: String,
    @JsonProperty("text") val text: String,
    @JsonProperty("author_id") val author_id: Int
)
