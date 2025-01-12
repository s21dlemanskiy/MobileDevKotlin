package com.example.task37.viewmodel

import android.app.Application
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
    private var _songs = MutableStateFlow<List<Song>>(emptyList())
    // это делается для того чтобы observer не мог менять его
    val songs: StateFlow<List<Song>> = _songs

    private var _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading
    private var _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage
    private var _dataFromDB = MutableStateFlow<Boolean>(false)
    val dataFromDB = _dataFromDB

    private val applicationComponent: ApplicationComponent = App.get(getApplication()).applicationComponent
    private val songsController = SongsController(applicationComponent)

    init {
        updateSongs()
    }

    fun removeSong(id: Int){
        _loading.value = true
        viewModelScope.launch {
            val songs = songsController.removeSong(id){ code ->
                errorMessage.value = "$code fetch data error"
            }
            _songs.value = songs
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
            _songs.value = songs
        }.invokeOnCompletion {
            _loading.value = false
            _dataFromDB.value = false
        }
    }

    fun extractSongsFromLocalDB() {
        _errorMessage.value = null
        _loading.value = true
        viewModelScope.launch {
            val songs = songsController.extractSongsFromLocalDB()
            _songs.value = songs.orEmpty()
        }.invokeOnCompletion {
            _loading.value = false
            _dataFromDB.value = true
        }
    }

}