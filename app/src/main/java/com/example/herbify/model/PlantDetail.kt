package com.example.herbify.model

import com.google.gson.annotations.SerializedName

data class PlantDetail(
    @SerializedName("id")                    val id: Int,
    @SerializedName("common_name")           val commonName: String?,
    @SerializedName("scientific_name")       val scientificName: List<String>?,
    @SerializedName("other_name")            val otherName: List<String>?,
    @SerializedName("family")               val family: String?,
    @SerializedName("origin")               val origin: List<String>?,
    @SerializedName("type")                 val type: String?,
    @SerializedName("cycle")               val cycle: String?,
    @SerializedName("watering")             val watering: String?,
    @SerializedName("sunlight")             val sunlight: List<String>?,
    @SerializedName("maintenance")          val maintenance: String?,
    @SerializedName("hardiness")            val hardiness: Hardiness?,
    @SerializedName("flowers")             val flowers: Boolean?,
    @SerializedName("flowering_season")     val floweringSeason: String?,
    @SerializedName("soil")               val soil: List<String>?,
    @SerializedName("growth_rate")          val growthRate: String?,
    @SerializedName("care_level")           val careLevel: String?,
    @SerializedName("indoor")              val indoor: Boolean?,
    @SerializedName("edible_leaf")          val edibleLeaf: Boolean?,
    @SerializedName("edible_fruit")         val edibleFruit: Boolean?,
    @SerializedName("medicinal")            val medicinal: Boolean?,
    @SerializedName("poisonous_to_humans")  val poisonousToHumans: Any?,
    @SerializedName("poisonous_to_pets")    val poisonousToPets: Any?,
    @SerializedName("description")          val description: String?,
    @SerializedName("default_image")        val defaultImage: DefaultImage?
)

data class Hardiness(
    @SerializedName("min") val min: String?,
    @SerializedName("max") val max: String?
)