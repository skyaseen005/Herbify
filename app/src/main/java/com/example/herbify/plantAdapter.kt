package com.example.herbify

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlantAdapter(private val context: Context, private var plantList: List<PlantData>) :
    RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantImage: ImageView = itemView.findViewById(R.id.logoIv)
        val plantName: TextView = itemView.findViewById(R.id.plantNameTv)
        val scientificName: TextView = itemView.findViewById(R.id.scientificNameTv)
        val description: TextView = itemView.findViewById(R.id.descriptionTv)
        val readMoreBtn: Button = itemView.findViewById(R.id.readMoreBtn)
    }

    fun setFilteredList(filteredList: List<PlantData>) {
        this.plantList = filteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_tem, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantList[position]
        holder.plantImage.setImageResource(plant.imageResId)
        holder.plantName.text = plant.name
        holder.scientificName.text = plant.scientificName
        holder.description.text = plant.description

        // Create Intent to pass data
        val intent = Intent(context, PlantDetailActivity::class.java).apply {
            putExtra("plantName", plant.name)
            putExtra("scientificName", plant.scientificName)
            putExtra("description", plant.description)
            putExtra("imageResId", plant.imageResId)
            putExtra("properties", plant.properties) // Pass the new properties
        }

        // Handle button click to open the detail activity
        holder.readMoreBtn.setOnClickListener {
            context.startActivity(intent)
        }

        // Handle image and name clicks
        holder.plantImage.setOnClickListener {
            context.startActivity(intent)
        }
        holder.plantName.setOnClickListener {
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = plantList.size // Corrected this line
}
