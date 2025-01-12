package com.example.task39.model

import android.util.Log
import com.example.task39.model.SongRetrofitService.Companion.safeApiCall
import com.example.task39.model.models.RawAuthor
import com.example.task39.model.models.RawSong
import com.example.task39.viewmodel.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class SongsRepositoryImpl @Inject constructor(
    private val api: SongRetrofitService,
    private val songsDAO: SongsDAO
): SongsRepository {


    private fun List<RawSong>.mapToDatabaseSongsModel(authors: Map<Int, RawAuthor>): List<com.example.task39.model.models.Song> {
        return map {
            com.example.task39.model.models.Song(
                id = it.id,
                title = it.title,
                text = it.text,
                author = authors.getOrDefault(it.author_id, null)?.name ?: "no author",
                authorId = it.author_id
            )
        }
    }

    private fun List<com.example.task39.model.models.Song>.mapToViewModelSongsModel(): List<Song>{
        return map {
            Song(
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
    private suspend fun updateCache(songs: List<com.example.task39.model.models.Song>): List<com.example.task39.model.models.Song> {
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
    private suspend fun getSongsWithText(text: String): List<com.example.task39.model.models.Song> {
        Log.i("DB", "Search songs with $text")
        return withContext(Dispatchers.IO) {
            songsDAO.getSongsWithText(text)
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
    override suspend fun getSongs(onResponseFailure: (code: Int) -> Unit): List<Song> = coroutineScope {
        var songs: List<com.example.task39.model.models.Song>

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
    override suspend fun removeSong(id: Int, onResponseFailure: (code: Int) -> Unit): List<Song> = coroutineScope {
        var songs: List<com.example.task39.model.models.Song>
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


    /**
     * By text find songs that contains it (or author or title contains this text)
     *
     * @property searchText text to be matched
     * @return list of songs
     * */
    override suspend fun searchSongsInLocalDB(searchText: String): List<Song>? = coroutineScope {
        try {
            getSongsWithText(searchText).mapToViewModelSongsModel()
        } catch (e: IllegalStateException){
            Log.w("DB", "Error interaction with DB $e")
            null
        } catch (e: Exception){
            Log.w("DB", "Error interaction with DB $e")
            null
        }
    }
}