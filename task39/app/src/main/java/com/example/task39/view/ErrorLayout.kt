package com.example.task39.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task39.R
import com.example.task39.viewmodel.SongViewModel

@Composable
fun ErrorLayout(
    error: String?,
    padding: PaddingValues,
    songViewModel: SongViewModel =  hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(text = error!!)
            Button(onClick = { songViewModel.stopLoadingAndExtractSongsFromLocalDB() }) {
                Text(text = stringResource(R.string.take_data_from_db))
            }
        }
    }
}