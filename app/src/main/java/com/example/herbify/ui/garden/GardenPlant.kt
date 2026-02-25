package com.example.herbify.ui.garden



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garden_plants")
data class GardenPlant(
    @PrimaryKey val id: Int,
    val commonName: String,
    val scientificName: String,
    val imageUrl: String?,
    val watering: String?,
    val cycle: String?,
    val sunlight: String?,
    val addedAt: Long = System.currentTimeMillis()
)