package com.example.task39.model

import com.example.task39.model.models.RawAuthor
import com.example.task39.model.models.RawSong
import kotlinx.coroutines.CancellationException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.net.ConnectException
import java.net.SocketTimeoutException

interface SongRetrofitService {
        @GET("/songs")
        suspend fun getSongs(): Response<List<RawSong>>

        @GET("/authors")
        suspend fun getAuthors(): Response<List<RawAuthor>>

        @GET("/remove/song/{id}")
        suspend fun deleteSong(@Path("id") id: Int): Response<List<RawSong>>

        @GET("/remove/author/{id}")
        suspend fun deleteAuthor(@Path("id") id: Int): Response<List<RawAuthor>>

        companion object {
                @JvmStatic
                suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
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
        }

}