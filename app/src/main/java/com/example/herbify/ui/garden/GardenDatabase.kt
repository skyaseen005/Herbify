package com.example.herbify.ui.garden


import android.content.Context
import androidx.room.*

@Dao
interface GardenDao {
    @Query("SELECT * FROM garden_plants ORDER BY addedAt DESC")
    suspend fun getAllPlants(): List<GardenPlant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: GardenPlant)

    @Delete
    suspend fun deletePlant(plant: GardenPlant)

    @Query("SELECT EXISTS(SELECT 1 FROM garden_plants WHERE id = :plantId)")
    suspend fun isPlantInGarden(plantId: Int): Boolean

    @Query("SELECT COUNT(*) FROM garden_plants")
    suspend fun getPlantCount(): Int
}

@Database(entities = [GardenPlant::class], version = 1, exportSchema = false)
abstract class GardenDatabase : RoomDatabase() {
    abstract fun gardenDao(): GardenDao

    companion object {
        @Volatile
        private var INSTANCE: GardenDatabase? = null

        fun getDatabase(context: Context): GardenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GardenDatabase::class.java,
                    "garden_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}