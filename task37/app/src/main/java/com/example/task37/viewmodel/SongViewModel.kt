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

    private val applicationComponent: ApplicationComponent = App.get(getApplication()).applicationComponent
    private val songsController = SongsController(applicationComponent)

    init {
        updateSongs()
    }

    fun removeSong(id: Int){
        _loading.value = true
        viewModelScope.launch {
            _songs.value = songsController.removeSong(id)
        }.invokeOnCompletion { _loading.value = false }
    }

    fun updateSongs() {
        _loading.value = true
        viewModelScope.launch {
            _songs.value = songsController.getSongs()
        }.invokeOnCompletion { _loading.value = false }
    }

}