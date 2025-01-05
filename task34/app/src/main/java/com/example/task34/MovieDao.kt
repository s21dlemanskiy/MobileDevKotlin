package com.example.task34
import androidx.room.*
import com.example.task34.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE :cinema is NULL OR cinema = :cinema")
    fun getMoviesByFilter(cinema: String): List<Movie>

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)
}