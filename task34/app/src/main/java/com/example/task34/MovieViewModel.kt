package com.example.task34
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task34.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(private val movieDao: MovieDao) : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> get() = _movies

    init {
        viewModelScope.launch {
            _movies.value = movieDao.getAllMovies()
        }
    }

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            movieDao.insertMovie(movie)
            _movies.value = movieDao.getAllMovies()
        }
    }

    // Implement update and delete functions similarly
}