package com.example.task37.model

import android.system.ErrnoException
import android.util.Log
import com.example.task37.ioc.ApplicationComponent
import com.example.task37.model.SongRetrofitService.Companion.safeApiCall
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
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
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

    /**
     * Insert data in database and then get it from database
     *
     * @property songs data to be insert in DB
     * @return all data from songs DB
     * @throws IllegalStateException неправильное состояние базы данных
     * @throws SQLiteConstraintException нарушение ограничений БД
     */
    private suspend fun updateCache(songs: List<DatabaseSongsModel>): List<DatabaseSongsModel> {
        return withContext(Dispatchers.IO) {
            songsDAO.clearAndInsert(songs)
            songsDAO.getSongs()
        }
    }

    /**
     * Get songs from DB
     *
     * @return all data from songs DB
     * @throws IllegalStateException неправильное состояние базы данных
     * @throws SQLiteConstraintException нарушение ограничений БД
     */
    private suspend fun getSongsFromCache(): List<DatabaseSongsModel> {
        return withContext(Dispatchers.IO) {
            songsDAO.getSongs()
        }
    }

    /**
     * Extract response from wrappers Result and Response
     *
     * @property response response in wrap
     * @property onResponseFailure callback to be performon Response failure with any not 200 code
     * @return response without wrap
     * @throws IllegalStateException неправильное состояние базы данных
     * @throws SQLiteConstraintException нарушение ограничений БД
     */
    private inline fun <reified T> getResponse(response: Result<Response<T>>, onResponseFailure: (code: Int) -> Unit): T?{
        if (response.isSuccess) {
            if (response.getOrThrow().isSuccessful) {
                val result = response.getOrThrow().body()
                return result
            } else {
                Log.w("WEB", "Error in response to get ${T::class.java.name} error code: ${response.getOrNull()?.code()} response:${response.getOrNull()?.raw()}")
                if (response.getOrNull()?.code()?.let{ it != 200 } == true) {
                    onResponseFailure(response.getOrThrow().code())
                }
            }
        }
        Log.w("WEB", "Error in response to get ${T::class.java.name}")
        return null
    }

    /**
     * Request songs list from web server
     *
     * @property onResponseFailure callback to be performon Response failure with any not 200 code
     * @return list of songs
     * */
    suspend fun getSongs(onResponseFailure: (code: Int) -> Unit): List<ViewModelSongsModel> = coroutineScope {
        var songs: List<DatabaseSongsModel>

        while(true) {
            val rawSongsDeferred = async { safeApiCall {api.getSongs()} }
            val rawAuthorDeferred = async { safeApiCall {api.getAuthors()} }
            val songsResult = rawSongsDeferred.await()
            val authorsResult = rawAuthorDeferred.await()
            val rawSongs = getResponse(songsResult, onResponseFailure)
            val rawAuthors = getResponse(authorsResult, onResponseFailure)
            if (rawSongs == null || rawAuthors == null){
                continue
            }
            songs = rawSongs.mapToDatabaseSongsModel(rawAuthors.associateBy { it.id })
            break
        }
        try {
            songs = updateCache(songs)
        } catch (e: IllegalStateException){
            Log.w("DB", "Error interaction with DB $e")
        } catch (e: Exception){
            Log.w("DB", "Error interaction with DB $e")
        }
        songs.mapToViewModelSongsModel()
    }

    /**
     * Request remove song with id to web server and then update list of songs
     *
     * @property id id of song to be removed
     * @property onResponseFailure callback to be performon Response failure with any not 200 code
     * @return list of songs
     * */
    suspend fun removeSong(id: Int, onResponseFailure: (code: Int) -> Unit): List<ViewModelSongsModel> = coroutineScope {
        var songs: List<DatabaseSongsModel>
        while(true) {
            val rawSongsDeferred = async { safeApiCall {api.deleteSong(id)} }
            val rawAuthorDeferred = async { safeApiCall {api.getAuthors()} }
            val songsResult = rawSongsDeferred.await()
            val authorsResult = rawAuthorDeferred.await()
            val rawSongs = getResponse(songsResult, onResponseFailure)
            val rawAuthors = getResponse(authorsResult, onResponseFailure)
            if (rawSongs == null || rawAuthors == null){
                continue
            }
            songs = rawSongs.mapToDatabaseSongsModel(rawAuthors.associateBy { it.id })
            break
        }
        try {
            songs = updateCache(songs)
        } catch (e: IllegalStateException){
            Log.w("DB", "Error interaction with DB $e")
        } catch (e: Exception){
            Log.w("DB", "Error interaction with DB $e")
        }
        songs.mapToViewModelSongsModel()
    }

    suspend fun extractSongsFromLocalDB(): List<ViewModelSongsModel>? = coroutineScope {
        try {
            getSongsFromCache().mapToViewModelSongsModel()
        } catch (e: IllegalStateException){
            Log.w("DB", "Error interaction with DB $e")
            null
        } catch (e: Exception){
            Log.w("DB", "Error interaction with DB $e")
            null
        }
    }
}