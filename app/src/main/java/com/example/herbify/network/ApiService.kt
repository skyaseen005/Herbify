package com.example.herbify.network




import com.example.herbify.model.PlantDetail
import com.example.herbify.model.PlantListResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("species-list")
    suspend fun getPlantList(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("q") query: String? = null,
        @Query("edible") edible: Int? = null,
        @Query("poisonous") poisonous: Int? = null,
        @Query("cycle") cycle: String? = null,
        @Query("watering") watering: String? = null,
        @Query("sunlight") sunlight: String? = null,
        @Query("indoor") indoor: Int? = null,
        @Query("order") order: String? = null
    ): Response<PlantListResponse>

    @GET("species/details/{id}")
    suspend fun getPlantDetail(
        @Path("id") plantId: Int,
        @Query("key") apiKey: String
    ): Response<PlantDetail>
}