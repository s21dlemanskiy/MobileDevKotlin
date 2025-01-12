package com.example.task39.model

import com.example.task39.viewmodel.models.Song

interface SongsRepository {
    suspend fun getSongs(onResponseFailure: (code: Int) -> Unit): List<Song>
    suspend fun removeSong(id: Int, onResponseFailure: (code: Int) -> Unit): List<Song>
    suspend fun searchSongsInLocalDB(searchText: String): List<Song>?
}