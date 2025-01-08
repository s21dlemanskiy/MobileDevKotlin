package com.example.task37.model

import com.example.task37.ioc.ApplicationComponent
import com.example.task37.model.models.RawAuthor
import com.example.task37.model.models.RawSong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.task37.model.models.Song as DatabaseSongsModel
import com.example.task37.viewmodel.models.Song as ViewModelSongsModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

class SongsController(
    private val applicationComponent: ApplicationComponent
) {
    private val api = applicationComponent.api
    private val songsDAO = applicationComponent.songsDAO

    private fun List<RawSong>.mapToDatabaseSongsModel(authors: Map<Int, RawAuthor>): List<DatabaseSongsModel> {
         return map { DatabaseSongsModel(
            id = it.id,
            title = it.title,
            text = it.text,
            author = authors.getOrDefault(it.author_id, null)?.name ?: "no author",
            authorId = it.author_id
        ) }
    }

    private fun List<DatabaseSongsModel>.mapToViewModelSongsModel(): List<ViewModelSongsModel>{
        return map {
            ViewModelSongsModel(
                id = it.id,
                title = it.title,
                text = it.text,
                author = it.author
            )
        }
    }

    private suspend fun updateCache(songs: List<DatabaseSongsModel>): List<DatabaseSongsModel> =
        withContext(Dispatchers.IO) {
            songsDAO.clearAndInsert(songs)
            songsDAO.getSongs()
        }

    suspend fun getSongs(): List<ViewModelSongsModel> = coroutineScope {
        val rawSongsDeferred = async{ api.getSongs() }
        val rawAuthorDeferred = async{ api.getAuthors() }
        val rawSongs = rawSongsDeferred.await()
        val rawAuthorDict = rawAuthorDeferred.await().associateBy { it.id }

        var songs = rawSongs.mapToDatabaseSongsModel(rawAuthorDict)
        songs = updateCache(songs)
        // return@coroutineScope
        songs.mapToViewModelSongsModel()
    }

    suspend fun removeSong(id: Int): List<ViewModelSongsModel> = coroutineScope {
        val rawSongsDeferred = async{ api.deleteSong(id) }
        val rawAuthorDeferred = async{ api.getAuthors() }
        val rawSongs = rawSongsDeferred.await()
        val rawAuthorDict = rawAuthorDeferred.await().associateBy { it.id }

        var songs = rawSongs.mapToDatabaseSongsModel(rawAuthorDict)
        songs = updateCache(songs)
        // return@coroutineScope
        songs.mapToViewModelSongsModel()
    }
}