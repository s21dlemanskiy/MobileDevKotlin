package com.example.task37.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import com.example.task37.viewmodel.models.Song

@Composable
fun SongListScreen(songViewModel: SongViewModel = viewModel()) {
    val songs by songViewModel.songs.collectAsState()

    LazyColumn {
        items(songs) { song ->
            SongItem(song = song, onRemove = { songViewModel.removeSong(song.id) })
        }
    }
}

@Composable
fun SongItem(song: Song, onRemove: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "${song.title}", style = MaterialTheme.typography.titleMedium)
        Text(text = "${song.text}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Автор: ${song.author}", style = MaterialTheme.typography.labelSmall)
        Button(
            onClick = onRemove,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Remove")
        }
    }
}