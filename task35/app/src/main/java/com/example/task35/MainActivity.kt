package com.example.task35

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.task35.ui.theme.Task35Theme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "animal_facts_db"
        ).build()
        enableEdgeToEdge()
        setContent {
            Task35Theme {
                var animal by rememberSaveable {
                    mutableStateOf("cat")
                }
                val viewModel: AnimalFactViewModel by viewModels {
                    object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return AnimalFactViewModel(database) as T
                        }
                    }
                }

                Column {
                    TextField(value = animal, onValueChange = {animal = it.toString()})
                    AnimalFactScreen(viewModel, animal)

                }
            }
        }
    }


    @Composable
    fun AnimalFactScreen(viewModel: AnimalFactViewModel, animalType: String) {
        val facts = rememberSaveable{ mutableStateOf(emptyList<AnimalFact>()) }

        Button(onClick = {
            lifecycleScope.launch {
                viewModel.fetchAndCacheFacts(animalType)
                val f = viewModel.getFacts(animalType)
                Log.i("getData", "facts from db:$f")
                facts.value = f
            }
        }) {
            Text(text = "upd")
        }

//    LaunchedEffect(animalType) {
//            viewModel.fetchAndCacheFacts(animalType)
//            val f = viewModel.getFacts(animalType)
//            Log.i("getData", "facts from db:$f")
//            facts.value = f
//    }

        LazyColumn {
            items(facts.value) { fact ->
                Text(text = fact.text)
            }
        }
    }
}
