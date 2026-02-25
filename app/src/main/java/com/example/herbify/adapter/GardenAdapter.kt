package com.example.herbify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.herbify.R                                    // ✅ correct R import
import com.example.herbify.databinding.ItemGardenPlantBinding
import com.example.herbify.ui.garden.GardenPlant

class GardenAdapter(
    private var plants: List<GardenPlant>,
    private val onRemoveClick: (GardenPlant) -> Unit,
    private val onItemClick: (GardenPlant) -> Unit
) : RecyclerView.Adapter<GardenAdapter.GardenViewHolder>() {

    // ✅ removed 'inner' — not needed since we don't access outer class members
    class GardenViewHolder(val binding: ItemGardenPlantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenViewHolder {
        val binding = ItemGardenPlantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GardenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GardenViewHolder, position: Int) {
        val plant = plants[position]
        with(holder.binding) {
            tvGardenName.text = plant.commonName.replaceFirstChar { it.uppercase() }
            tvGardenScientific.text = plant.scientificName
            tvGardenWatering.text = "💧 ${plant.watering?.replaceFirstChar { it.uppercase() } ?: "—"}"
            tvGardenCycle.text = plant.cycle?.replaceFirstChar { it.uppercase() } ?: "—"

            Glide.with(ivGardenPlant.context)
                .load(plant.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder_plant)   // ✅ now resolves correctly
                .error(R.drawable.placeholder_plant)         // ✅ now resolves correctly
                .centerCrop()
                .into(ivGardenPlant)

            btnRemove.setOnClickListener { onRemoveClick(plant) }
            root.setOnClickListener { onItemClick(plant) }
        }
    }

    override fun getItemCount() = plants.size

    fun updatePlants(newPlants: List<GardenPlant>) {
        plants = newPlants
        notifyDataSetChanged()
    }
}