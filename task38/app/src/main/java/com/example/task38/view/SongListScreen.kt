package com.example.task38.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task38.viewmodel.SongViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.task38.R


@Composable
fun SongListScreen(padding: PaddingValues, songViewModel: SongViewModel = viewModel()) {
    val loading by songViewModel.loading.collectAsState()
    val error by songViewModel.errorMessage.collectAsState()
    if (error != null){
        ErrorLayout(error, padding, songViewModel)
        return
    }
    if (loading) {
        LoadingLayout(padding, songViewModel)
        return
    }
    Content(padding, songViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    padding: PaddingValues = PaddingValues.Absolute(),
    songViewModel: SongViewModel = viewModel()
    ){
    val songs by songViewModel.songs.collectAsState()
    val dataFromDB by songViewModel.dataFromDB.collectAsState()
    val searchText by songViewModel.searchText.collectAsState()
    var active by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(padding)
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            query = searchText,
            onQueryChange = { songViewModel.setSearchText(it) },
            onSearch = {
                active = false
                songViewModel.search()
                       },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(stringResource(R.string.search_text)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_description)
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        songViewModel.setSearchText("")
                        songViewModel.search()
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search_descritpion)
                        )

                    }
                }
            }
        ) {}
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

