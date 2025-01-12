package com.example.task34

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.task34.ui.theme.Task34Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.lifecycleScope
import com.example.task34.AppDatabase
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task34Theme {
                AppNavigation()
            }
        }
    }
    @Composable
    fun AppNavigation() {
        database = Room.databaseBuilder(
            this as Context,
            AppDatabase::class.java,
            "my_db"
        ).build()
        val movies = MutableStateFlow<List<Movie>>(emptyList())
        val navController = rememberNavController()
        NavHost(navController, startDestination = "movieList") {
            composable("movieList") { MovieListScreen(navController, movies) }
            composable("addMovie") { AddMovieScreen(navController, movies) }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun MovieListScreen(navController: NavController, movies: MutableStateFlow<List<Movie>>) {
        LaunchedEffect(Unit) {
            loadMovies(movies)
        }
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Movie List") })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("addMovie") }) {
                    Text("+")
                }
            }
        ) { padding ->
            Column (
                modifier = Modifier.padding(padding)
            ) {
                movies.value.forEach { movie ->
                    // пока ничего не делаем
                    MovieItem(movie) {}
                }
            }
        }
    }

    @Composable
    fun MovieItem(movie: Movie, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onClick() }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Film: ${movie.film}")
                Text(text = "Cinema: ${movie.cinema}")
                Text(text = "Date: ${movie.date}, Time: ${movie.time}")
                Text(text = "Hall: ${movie.hallNumber}, Type: ${movie.sessionType}")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddMovieScreen(navController: NavController, movies: MutableStateFlow<List<Movie>>) {
        var date by rememberSaveable {mutableStateOf("")}
        var time by rememberSaveable { mutableStateOf("") }
        var cinema by rememberSaveable { mutableStateOf("") }
        var film by rememberSaveable { mutableStateOf("") }
        var hallNumber by rememberSaveable { mutableStateOf("") }
        var sessionType by rememberSaveable { mutableStateOf(SessionType.REGULAR) }
        var expanded by rememberSaveable { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Add Movie") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") }
                )
                TextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") }
                )
                TextField(
                    value = cinema,
                    onValueChange = { cinema = it },
                    label = { Text("Cinema") }
                )
                TextField(
                    value = film,
                    onValueChange = { film = it },
                    label = { Text("Film") }
                )
                TextField(
                    value = hallNumber,
                    onValueChange = { hallNumber = it.toString() },
                    label = { Text("Hall Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Box {
                    Button(onClick = { expanded = !expanded }) {
                        Text(text = "Session Type: $sessionType")
                    }
//                    IconButton(onClick = { expanded = true }) {
//                        Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
//                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        SessionType.entries.forEach { type ->
                            DropdownMenuItem(
                                onClick = {
                                    sessionType = type
                                    expanded = false
                                },
                                text = {
                                    Text(text = type.name)
                                }
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        insertMovie(
                            Movie(
                                date = date,
                                time = time,
                                cinema = cinema,
                                film = film,
                                hallNumber = hallNumber.toIntOrNull() ?: 0,
                                sessionType = sessionType
                            )
                        ){
                            navController.popBackStack()
                            loadMovies(movies)
                        }
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }

    private fun insertMovie(movie: Movie, after: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            database.movieDao().insertMovie(movie)
            withContext(Dispatchers.Main) {
                after()
            }
        }
    }
    private fun loadMovies(movies: MutableStateFlow<List<Movie>>) {
        lifecycleScope.launch(Dispatchers.IO) {
            val movieList = database.movieDao().getAllMovies()
            withContext(Dispatchers.Main) {
                movies.value = movieList
                Log.i("movies", "updated")
            }
        }
    }


}






