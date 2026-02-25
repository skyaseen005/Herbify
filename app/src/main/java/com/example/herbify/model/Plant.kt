package com.example.herbify.model

import android.annotation.SuppressLint
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class PlantListResponse(
    @SerializedName("data") val data: List<Plant>,
    @SerializedName("to") val to: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("total") val total: Int
)

@SuppressLint("ParcelCreator")
@Parcelize
data class Plant(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: List<String>?,
    @SerializedName("other_name") val otherName: List<String>?,
    @SerializedName("cycle") val cycle: String?,
    @SerializedName("watering") val watering: String?,
    @SerializedName("sunlight") val sunlight: List<String>?,
    @SerializedName("default_image") val defaultImage: DefaultImage?,
    @SerializedName("edible_leaf") val edibleLeaf: Boolean?,
    @SerializedName("poisonous_to_humans") val poisonousToHumans: Int?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class DefaultImage(
    @SerializedName("original_url") val originalUrl: String?,
    @SerializedName("regular_url") val regularUrl: String?,
    @SerializedName("medium_url") val mediumUrl: String?,
    @SerializedName("small_url") val smallUrl: String?,
    @SerializedName("thumbnail") val thumbnail: String?
) : Parcelable