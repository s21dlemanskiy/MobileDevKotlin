package com.example.task37.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task37.viewmodel.SongViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import com.example.task37.viewmodel.models.Song

@Composable
fun SongListScreen(songViewModel: SongViewModel = viewModel()) {
    val songs by songViewModel.songs.collectAsState()
    val loading by songViewModel.loading.collectAsState()
    val error by songViewModel.errorMessage.collectAsState()
    if (error != null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = error!!)
        }
        return
    }
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        return
    }

    LazyColumn {
        items(songs) { song ->
            SongItem(song = song, onRemove = { songViewModel.removeSong(song.id) })
        }
    }
}

@Composable
fun SongItem(song: Song, onRemove: () -> Unit) {
    Log.i("SongItem", "create card with id: ${song.id} and song: $song")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("Tag:Card ${song.id}")
    ) {
        Text(
            text = "${song.title}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .testTag("Tag:Card title ${song.id}")
        )
        Text(
            text = "${song.text}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .testTag("Tag:Card text ${song.id}")
        )
        Text(
            text = "Автор: ${song.author}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .testTag("Tag:Card author ${song.id}")
        )
        Button(
            onClick = onRemove,
            modifier = Modifier
                .padding(top = 8.dp)
                .testTag("Tag:Card del button ${song.id}")
        ) {
            Text("Remove")
        }
    }
}