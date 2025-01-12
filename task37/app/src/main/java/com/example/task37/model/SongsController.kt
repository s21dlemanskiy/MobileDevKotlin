package com.example.task37.model

import android.system.ErrnoException
import android.util.Log
import com.example.task37.ioc.ApplicationComponent
import com.example.task37.model.models.RawAuthor
import com.example.task37.model.models.RawSong
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.task37.model.models.Song as DatabaseSongsModel
import com.example.task37.viewmodel.models.Song as ViewModelSongsModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.coroutines.EmptyCoroutineContext

class SongsController(
    private val applicationComponent: ApplicationComponent
) {
    private val api = applicationComponent.api
    private val songsDAO = applicationComponent.songsDAO

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: CancellationException) {
            throw e.cause ?: e // Пробрасываем причину отмены или саму отмену
        } catch (e: ConnectException) {
            Result.failure(e)
        } catch (e: SocketTimeoutException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


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


    suspend fun getSongs(onResponseFailure: (code: Int) -> Unit): List<ViewModelSongsModel>? = coroutineScope {
        var songs: List<DatabaseSongsModel>?
        while(true) {
            val rawSongsDeferred = async { safeApiCall {api.getSongs()} }
            val rawAuthorDeferred = async { safeApiCall {api.getAuthors()} }
            val getSongsResult = rawSongsDeferred.await()
            val getAuthorsResult = rawAuthorDeferred.await()
            if (getSongsResult.isSuccess && getAuthorsResult.isSuccess) {
                if (getSongsResult.getOrThrow().isSuccessful && getAuthorsResult.getOrThrow().isSuccessful) {
                    val rawSongs = getSongsResult.getOrThrow().body()!!
                    val rawAuthorDict = getAuthorsResult.getOrThrow().body()!!.associateBy { it.id }
                    songs = rawSongs.mapToDatabaseSongsModel(rawAuthorDict)
                    break
                } else {
                    if (getSongsResult.getOrNull()?.code()?.let{ it != 200 } == true) {
                        onResponseFailure(getSongsResult.getOrThrow().code())
                    }else if (getAuthorsResult.getOrNull()?.code()?.let{ it != 200 } == true){
                        onResponseFailure(getAuthorsResult.getOrThrow().code())
                    }
                }
            }
        }
        songs = updateCache(songs!!)
        // return@coroutineScope
        songs.mapToViewModelSongsModel()
    }

    suspend fun removeSong(id: Int, onResponseFailure: (code: Int) -> Unit): List<ViewModelSongsModel>? = coroutineScope {
        var songs: List<DatabaseSongsModel>?
        while(true) {
            val rawSongsDeferred = async { safeApiCall {api.deleteSong(id)} }
            val rawAuthorDeferred = async { safeApiCall {api.getAuthors()} }
            val getSongsResult = rawSongsDeferred.await()
            val getAuthorsResult = rawAuthorDeferred.await()
            if (getSongsResult.isSuccess && getAuthorsResult.isSuccess) {
                if (getSongsResult.getOrThrow().isSuccessful && getAuthorsResult.getOrThrow().isSuccessful) {
                    val rawSongs = getSongsResult.getOrThrow().body()!!
                    val rawAuthorDict = getAuthorsResult.getOrThrow().body()!!.associateBy { it.id }
                    songs = rawSongs.mapToDatabaseSongsModel(rawAuthorDict)
                    break
                } else {
                    if (getSongsResult.getOrNull()?.code()?.let{ it != 200 } == true) {
                        onResponseFailure(getSongsResult.getOrThrow().code())
                    }else if (getAuthorsResult.getOrNull()?.code()?.let{ it != 200 } == true){
                        onResponseFailure(getAuthorsResult.getOrThrow().code())
                    }
                }
            }
        }
        songs = updateCache(songs!!)
        // return@coroutineScope
        songs.mapToViewModelSongsModel()
    }
}