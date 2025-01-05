package com.example.task35
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimalFactViewModel(private val database: AppDatabase) : ViewModel() {

    fun fetchAndCacheFacts(animalType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("query", "start")
                // Fetch from API
                val facts = RetrofitInstance.api.getAnimalFacts(animalType)
                Log.i("query", "result: $facts")
                // Cache in database
                database.animalFactDao().insertAll(facts)
                Log.i("query", "inserted to DB")
            } catch (e: Exception) {
                Log.i("query", "error: $e")
                // Handle exceptions (e.g., no internet connection)
            }
        }
    }

    suspend fun getFacts(animalType: String): List<AnimalFact> {
        return withContext(Dispatchers.IO) {
            database.animalFactDao().getFactsByType(animalType)
        }
    }
}