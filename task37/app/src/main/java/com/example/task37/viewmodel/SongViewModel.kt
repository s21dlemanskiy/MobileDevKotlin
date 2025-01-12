package com.example.task37.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task37.App
import com.example.task37.ioc.ApplicationComponent
import com.example.task37.model.SongsController
import com.example.task37.viewmodel.models.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private var _allSongs = MutableStateFlow<List<Song>>(emptyList())
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

    private val applicationComponent: ApplicationComponent = App.get(getApplication()).applicationComponent
    private val songsController = SongsController(applicationComponent)

    init {
        Log.i("ViewModel", "View model ${this::class.java.name} created with hashCode: ${this.hashCode()}")
        updateSongs()
    }
    fun setSearchText(text: String){
        Log.i("ViewModel", "Set search text: $text")
        _searchText.value = text
    }

    private suspend fun performSearch(): List<Song>? =
        songsController.extractSongsFromLocalDB(searchText.value)

    fun search(){
        _loading.value = true
        viewModelScope.launch {
            _songs.value = performSearch().orEmpty()
        }.invokeOnCompletion {
            _loading.value = false
        }
    }

    fun removeSong(id: Int){
        _loading.value = true
        viewModelScope.launch {
            val songs = songsController.removeSong(id){ code ->
                errorMessage.value = "$code fetch data error"
            }
            Log.i("ViewModel", "get songs: $songs")
            _songs.value = performSearch().orEmpty()
        }.invokeOnCompletion {
            _loading.value = false
            _dataFromDB.value = false
        }
    }

    fun updateSongs() {
        _loading.value = true
        viewModelScope.launch {
            val songs = songsController.getSongs(){ code ->
                errorMessage.value = code.toString()
            }
            Log.i("ViewModel", "get songs: $songs")
            _songs.value = performSearch().orEmpty()
        }.invokeOnCompletion {
            _loading.value = false
            _dataFromDB.value = false
        }
    }

    fun stopLoadingAndExtractSongsFromLocalDB() {
        _errorMessage.value = null
        _loading.value = true
        viewModelScope.launch {
            _songs.value = performSearch().orEmpty()
        }.invokeOnCompletion {
            _loading.value = false
            _dataFromDB.value = true
        }
    }

}