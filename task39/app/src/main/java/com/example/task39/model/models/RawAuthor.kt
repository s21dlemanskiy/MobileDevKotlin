package com.example.task39.model.models

import com.fasterxml.jackson.annotation.JsonProperty

data class RawAuthor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String
)
