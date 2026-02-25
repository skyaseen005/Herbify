package com.example.herbify.ui.explore

import com.example.herbify.R
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.herbify.databinding.ActivityPlantDetailBinding
import com.example.herbify.ui.garden.GardenPlant
import com.example.herbify.viewmodel.GardenViewModel
import com.example.herbify.viewmodel.PlantViewModel

class PlantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantDetailBinding
    private val plantViewModel: PlantViewModel by viewModels()
    private val gardenViewModel: GardenViewModel by viewModels()
    private var plantId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        plantId = intent.getIntExtra("plant_id", -1)
        val plantName = intent.getStringExtra("plant_name") ?: "Plant"

        setupToolbar(plantName)
        observeData()

        if (plantId != -1) {
            plantViewModel.loadPlantDetail(plantId)
            gardenViewModel.checkIsInGarden(plantId)
        }
    }

    private fun setupToolbar(name: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = name
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun observeData() {
        plantViewModel.isLoading.observe(this) { loading ->
            binding.detailProgress.visibility = if (loading) View.VISIBLE else View.GONE
        }

        plantViewModel.plantDetail.observe(this) { detail ->
            detail ?: return@observe

            // Title
            binding.collapsingToolbar.title = detail.commonName?.replaceFirstChar { it.uppercase() } ?: "Plant"
            binding.tvDetailName.text = detail.commonName?.replaceFirstChar { it.uppercase() } ?: "Unknown"
            binding.tvDetailScientific.text = detail.scientificName?.firstOrNull() ?: ""

            // Info cards
            binding.tvSciNameCard.text = detail.scientificName?.firstOrNull() ?: getString(R.string.na)
            binding.tvFamilyCard.text = detail.family ?: getString(R.string.na)
            binding.tvWateringCard.text = detail.watering?.replaceFirstChar { it.uppercase() } ?: getString(R.string.na)
            binding.tvCycleCard.text = detail.cycle?.replaceFirstChar { it.uppercase() } ?: getString(R.string.na)
            binding.tvSunlightCard.text = detail.sunlight?.firstOrNull()?.replace("_", " ")
                ?.replaceFirstChar { it.uppercase() } ?: getString(R.string.na)
            binding.tvHardinessCard.text = if (detail.hardiness != null)
                "Zone ${detail.hardiness.min} - ${detail.hardiness.max}"
            else getString(R.string.na)

            // Badges
            binding.badgeCycle.text = detail.cycle?.replaceFirstChar { it.uppercase() } ?: "—"
            binding.badgeWatering.text = "💧 ${detail.watering?.replaceFirstChar { it.uppercase() } ?: "—"}"

            if (detail.edibleLeaf == true || detail.edibleFruit == true) {
                binding.badgeEdibleDetail.visibility = View.VISIBLE
            } else {
                binding.badgeEdibleDetail.visibility = View.GONE
            }

            // Poison warning
            if ((detail.poisonousToHumans ?: 0) > 0) {
                binding.cardWarning.visibility = View.VISIBLE
            }

            // Description
            binding.tvDescription.text = detail.description ?: "No description available for this plant."

            // Image
            Glide.with(this)
                .load(detail.defaultImage?.originalUrl ?: detail.defaultImage?.regularUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder_plant)
                .error(R.drawable.placeholder_plant)
                .into(binding.ivDetailImage)

            // Add to Garden
            binding.btnAddGarden.setOnClickListener {
                val gardenPlant = GardenPlant(
                    id = detail.id,
                    commonName = detail.commonName ?: "Unknown",
                    scientificName = detail.scientificName?.firstOrNull() ?: "—",
                    imageUrl = detail.defaultImage?.mediumUrl ?: detail.defaultImage?.originalUrl,
                    watering = detail.watering,
                    cycle = detail.cycle,
                    sunlight = detail.sunlight?.firstOrNull()
                )
                gardenViewModel.addToGarden(gardenPlant)
            }
        }

        gardenViewModel.isInGarden.observe(this) { inGarden ->
            if (inGarden) {
                binding.btnAddGarden.text = "✓ In My Garden"
                binding.btnAddGarden.isEnabled = false
            } else {
                binding.btnAddGarden.text = "🌱 Add to My Garden"
                binding.btnAddGarden.isEnabled = true
            }
        }

        gardenViewModel.message.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                gardenViewModel.clearMessage()
                gardenViewModel.checkIsInGarden(plantId)
            }
        }

        plantViewModel.error.observe(this) { err ->
            err?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                plantViewModel.clearError()
            }
        }
    }
}