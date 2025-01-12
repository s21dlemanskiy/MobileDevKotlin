package com.example.task37.model.models

import com.fasterxml.jackson.annotation.JsonProperty

data class RawAuthor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String
)
