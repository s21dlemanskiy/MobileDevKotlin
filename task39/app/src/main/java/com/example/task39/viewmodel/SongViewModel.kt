package com.example.task39.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task39.App
import com.example.task39.model.SongsRepository
import com.example.task39.model.SongsRepositoryImpl
import com.example.task39.viewmodel.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
    private val songsRepository: SongsRepository
) : ViewModel() {
    private var _songs = MutableStateFlow<List<Song>>(emptyList())
    // это делается для того чтобы observer не мог менять его
    val songs: StateFlow<List<Song>> = _songs

    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private var _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage
    private var _dataFromDB = MutableStateFlow(false)
    val dataFromDB = _dataFromDB

    private var _searchText = MutableStateFlow("")
    val searchText = _searchText

    init {
        Log.i("ViewModel", "View model ${this::class.java.name} created with hashCode: ${this.hashCode()}")
        updateSongs()
    }

    fun setSearchText(text: String){
        Log.i("ViewModel", "Set search text: $text")
        _searchText.value = text
    }

    private suspend fun performSearch(): List<Song>? =
        songsRepository.searchSongsInLocalDB(searchText.value)

    /**
     * Process code in viewModelScope adding _loading update in response start and end
     *
     * @property dataFromCache на что поменять _dataFromDB после завершения операции null соотвествует не менять
     * @property callback функция вызываемая внутри viewModelScope
     * */
    fun performSuspendRequest(dataFromCache: Boolean?, callback: suspend ()-> Unit) {
        _loading.value = true
        viewModelScope.launch {
            callback()
            _songs.value = performSearch().orEmpty()
            if (dataFromCache != null) _dataFromDB.value = dataFromCache
        }.invokeOnCompletion {
            _loading.value = false
        }
    }

    fun search(){
        performSuspendRequest(null) {
        }
    }


    fun removeSong(id: Int) {
        performSuspendRequest(false) {
            val songs = songsRepository.removeSong(id) { code ->
                errorMessage.value = code.toString()
            }
            Log.i("ViewModel", "get songs: $songs")
        }

    }

    fun updateSongs() {
        performSuspendRequest (false) {
            val songs = songsRepository.getSongs() { code ->
                errorMessage.value = code.toString()
            }
            Log.i("ViewModel", "get songs: $songs")
        }
    }

    fun stopLoadingAndExtractSongsFromLocalDB() {
        _errorMessage.value = null
        performSuspendRequest(true) {}
    }

}