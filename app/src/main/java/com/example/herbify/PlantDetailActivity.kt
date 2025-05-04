package com.example.herbify

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_detail)

        val imageView: ImageView = findViewById(R.id.detailImageView)
        val nameText: TextView = findViewById(R.id.detailName)
        val scientificText: TextView = findViewById(R.id.detailScientific)
        val descriptionText: TextView = findViewById(R.id.detailDescription)
        val propertiesText: TextView = findViewById(R.id.detailProperties) // New TextView for properties

        // Retrieve the data from the intent
        val name = intent.getStringExtra("plantName")
        val scientificName = intent.getStringExtra("scientificName")
        val description = intent.getStringExtra("description")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.neem)
        val properties = intent.getStringExtra("properties") // Retrieve properties

        // Set the values to UI elements
        imageView.setImageResource(imageResId)
        nameText.text = name
        scientificText.text = scientificName
        descriptionText.text = description
        propertiesText.text = properties // Set the properties to the new TextView
    }
}
