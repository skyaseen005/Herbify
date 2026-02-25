package com.example.herbify.viewmodel

import com.example.herbify.model.Plant
import com.example.herbify.model.PlantDetail
import com.example.herbify.network.RetrofitClient


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.Retrofit
import kotlinx.coroutines.launch

class PlantViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService
    private val apiKey = RetrofitClient.API_KEY

    private val _plants = MutableLiveData<List<Plant>>()
    val plants: LiveData<List<Plant>> = _plants

    private val _plantDetail = MutableLiveData<PlantDetail?>()
    val plantDetail: LiveData<PlantDetail?> = _plantDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _hasMorePages = MutableLiveData<Boolean>()
    val hasMorePages: LiveData<Boolean> = _hasMorePages

    private var currentPage = 1
    private var lastQuery: String? = null
    private var lastEdible: Int? = null
    private var lastCycle: String? = null
    private var lastSunlight: String? = null
    private var lastIndoor: Int? = null
    private val allPlants = mutableListOf<Plant>()

    init {
        loadPlants()
    }

    fun loadPlants(
        query: String? = null,
        edible: Int? = null,
        cycle: String? = null,
        sunlight: String? = null,
        indoor: Int? = null,
        reset: Boolean = false
    ) {
        if (reset) {
            currentPage = 1
            allPlants.clear()
            lastQuery = query
            lastEdible = edible
            lastCycle = cycle
            lastSunlight = sunlight
            lastIndoor = indoor
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getPlantList(
                    apiKey = apiKey,
                    page = currentPage,
                    query = lastQuery,
                    edible = lastEdible,
                    cycle = lastCycle,
                    sunlight = lastSunlight,
                    indoor = lastIndoor
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        allPlants.addAll(it.data)
                        _plants.value = allPlants.toList()
                        _hasMorePages.value = currentPage < it.lastPage
                        currentPage++
                    }
                } else {
                    _error.value = "Failed to load plants: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMorePlants() {
        loadPlants()
    }

    fun searchPlants(query: String) {
        loadPlants(query = query, reset = true)
    }

    fun filterPlants(
        edible: Int? = null,
        cycle: String? = null,
        sunlight: String? = null,
        indoor: Int? = null
    ) {
        loadPlants(edible = edible, cycle = cycle, sunlight = sunlight, indoor = indoor, reset = true)
    }

    fun loadPlantDetail(plantId: Int) {
        _plantDetail.value = null
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getPlantDetail(plantId, apiKey)
                when {
                    response.isSuccessful -> {
                        _plantDetail.value = response.body()
                    }
                    response.code() == 401 -> {
                        _error.value = "API key invalid or expired"
                    }
                    response.code() == 429 -> {
                        _error.value = "API rate limit reached. Try again later."
                    }
                    response.code() == 403 -> {
                        _error.value = "This plant requires a premium API plan"
                    }
                    else -> {
                        _error.value = "Failed to load details (${response.code()})"
                    }
                }
            } catch (e: com.google.gson.JsonSyntaxException) {
                // ✅ catches the IllegalStateException from bad JSON
                _error.value = "API returned unexpected data. Check your API plan."
            } catch (e: java.io.IOException) {
                _error.value = "No internet connection"
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun clearError() {
        _error.value = null
    }
}