package com.example.task37.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.task37.R
import com.example.task37.viewmodel.models.Song
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SongListScreen(padding: PaddingValues, songViewModel: SongViewModel = viewModel()) {
    val songs by songViewModel.songs.collectAsState()
    val loading by songViewModel.loading.collectAsState()
    val error by songViewModel.errorMessage.collectAsState()
    val dataFromDB by songViewModel.dataFromDB.collectAsState()
    if (error != null){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(text = error!!)
                Button(onClick = { songViewModel.extractSongsFromLocalDB() }) {
                    Text(text = stringResource(R.string.take_data_from_db))
                }
            }
        }
        return
    }
    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .padding(vertical = 30.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Button(onClick = { songViewModel.extractSongsFromLocalDB() }) {
                    Text(text = stringResource(R.string.take_data_from_db))
                }
            }
        }
        return
    }
    Column(
        modifier = Modifier
        .padding(padding)
    ) {
        if (dataFromDB){
            Text(
                text = stringResource(R.string.not_representative_warning),
                Modifier
                    .background(color = Color.Yellow)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        LazyColumn {
            items(songs) { song ->
                SongItem(song = song, onRemove = { songViewModel.removeSong(song.id) })
            }
        }
    }
}

@Composable
fun SongItem(song: Song, onRemove: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) } // text expanded for text overflow
    Log.i("SongItem", "create card with id: ${song.id} and song: $song")
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("Tag:Card ${song.id}"),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp,
                hoveredElevation = 8.dp
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("Tag:Card title ${song.id}")
                    )

                    Text(
                        text = "Автор: ${song.author}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.testTag("Tag:Card author ${song.id}")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = song.text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag("Tag:Card text ${song.id}")
                        .clickable { expanded = !expanded },
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.testTag("Tag:Card del button ${song.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete song",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}