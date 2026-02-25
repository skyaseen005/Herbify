package com.example.herbify.viewmodel



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.herbify.ui.garden.GardenDatabase
import com.example.herbify.ui.garden.GardenPlant

import kotlinx.coroutines.launch

class GardenViewModel(application: Application) : AndroidViewModel(application) {

    private val db = GardenDatabase.getDatabase(application)
    private val gardenDao = db.gardenDao()

    private val _gardenPlants = MutableLiveData<List<GardenPlant>>()
    val gardenPlants: LiveData<List<GardenPlant>> = _gardenPlants

    private val _isInGarden = MutableLiveData<Boolean>()
    val isInGarden: LiveData<Boolean> = _isInGarden

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    init {
        loadGarden()
    }

    fun loadGarden() {
        viewModelScope.launch {
            _gardenPlants.value = gardenDao.getAllPlants()
        }
    }

    fun addToGarden(plant: GardenPlant) {
        viewModelScope.launch {
            val exists = gardenDao.isPlantInGarden(plant.id)
            if (!exists) {
                gardenDao.insertPlant(plant)
                loadGarden()
                _message.value = "Added to garden!"
            } else {
                _message.value = "Already in your garden"
            }
        }
    }

    fun removeFromGarden(plant: GardenPlant) {
        viewModelScope.launch {
            gardenDao.deletePlant(plant)
            loadGarden()
            _message.value = "Removed from garden"
        }
    }

    fun checkIsInGarden(plantId: Int) {
        viewModelScope.launch {
            _isInGarden.value = gardenDao.isPlantInGarden(plantId)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
