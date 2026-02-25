package com.example.herbify.model


import com.google.gson.annotations.SerializedName

data class PlantDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("common_name") val commonName: String?,
    @SerializedName("scientific_name") val scientificName: List<String>?,
    @SerializedName("other_name") val otherName: List<String>?,
    @SerializedName("family") val family: String?,
    @SerializedName("origin") val origin: List<String>?,
    @SerializedName("type") val type: String?,
    @SerializedName("dimensions") val dimensions: Dimensions?,
    @SerializedName("cycle") val cycle: String?,
    @SerializedName("watering") val watering: String?,
    @SerializedName("watering_period") val wateringPeriod: String?,
    @SerializedName("sunlight") val sunlight: List<String>?,
    @SerializedName("maintenance") val maintenance: String?,
    @SerializedName("hardiness") val hardiness: Hardiness?,
    @SerializedName("flowers") val flowers: Boolean?,
    @SerializedName("flowering_season") val floweringSeason: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("fruit_color") val fruitColor: List<String>?,
    @SerializedName("soil") val soil: List<String>?,
    @SerializedName("pest_susceptibility") val pestSusceptibility: List<String>?,
    @SerializedName("cones") val cones: Boolean?,
    @SerializedName("seeds") val seeds: Boolean?,
    @SerializedName("leaf") val leaf: Boolean?,
    @SerializedName("leaf_color") val leafColor: List<String>?,
    @SerializedName("edible_leaf") val edibleLeaf: Boolean?,
    @SerializedName("edible_fruit") val edibleFruit: Boolean?,
    @SerializedName("edible_fruit_taste_profile") val edibleFruitTasteProfile: String?,
    @SerializedName("fruit_nutritional_value") val fruitNutritionalValue: String?,
    @SerializedName("poisonous_to_humans") val poisonousToHumans: Int?,
    @SerializedName("poisonous_to_pets") val poisonousToPets: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("default_image") val defaultImage: DefaultImage?
)

data class Dimensions(
    @SerializedName("type") val type: String?,
    @SerializedName("min_value") val minValue: Double?,
    @SerializedName("max_value") val maxValue: Double?,
    @SerializedName("unit") val unit: String?
)

data class Hardiness(
    @SerializedName("min") val min: String?,
    @SerializedName("max") val max: String?
)