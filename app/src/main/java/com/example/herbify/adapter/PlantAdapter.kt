package com.example.herbify.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.herbify.R
import com.example.herbify.databinding.ItemPlantBinding
import com.example.herbify.model.Plant

class PlantAdapter(
    private var plants: List<Plant>,
    private val onPlantClick: (Plant) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(val binding: ItemPlantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        with(holder.binding) {
            tvPlantName.text = plant.commonName?.replaceFirstChar { it.uppercase() } ?: "Unknown"
            tvScientificName.text = plant.scientificName?.firstOrNull() ?: "—"
            tvCycleBadge.text = plant.cycle?.replaceFirstChar { it.uppercase() } ?: "—"
            tvWatering.text = plant.watering?.replaceFirstChar { it.uppercase() } ?: "—"
            tvSunlight.text = plant.sunlight?.firstOrNull()?.replace("_", " ")
                ?.replaceFirstChar { it.uppercase() } ?: "—"

            tvEdible.visibility = if (plant.edibleLeaf == true) View.VISIBLE else View.GONE

            Glide.with(ivPlant.context)
                .load(plant.defaultImage?.mediumUrl ?: plant.defaultImage?.originalUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder_plant)
                .error(R.drawable.placeholder_plant)
                .centerCrop()
                .into(ivPlant)

            root.setOnClickListener { onPlantClick(plant) }
        }
    }

    override fun getItemCount() = plants.size

    fun updatePlants(newPlants: List<Plant>) {
        plants = newPlants
        notifyDataSetChanged()
    }
}