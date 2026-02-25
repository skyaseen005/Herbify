package com.example.herbify.ui.explore

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.herbify.R
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

        plantId          = intent.getIntExtra("plant_id", -1)
        val plantName    = intent.getStringExtra("plant_name")      ?: "Plant"
        val plantCycle   = intent.getStringExtra("plant_cycle")     ?: ""
        val plantWater   = intent.getStringExtra("plant_watering")  ?: ""
        val plantSun     = intent.getStringExtra("plant_sunlight")  ?: ""
        val plantSci     = intent.getStringExtra("plant_scientific")?: ""
        val plantImage   = intent.getStringExtra("plant_image")     ?: ""
        val plantEdible  = intent.getBooleanExtra("plant_edible", false)

        setupToolbar(plantName)

        // Show basic data immediately while API loads
        showBasicInfo(plantName, plantSci, plantCycle, plantWater, plantSun, plantImage, plantEdible)

        if (plantId != -1) {
            plantViewModel.loadPlantDetail(plantId)
            gardenViewModel.checkIsInGarden(plantId)
        }

        observeViewModel(plantName, plantSci, plantImage, plantWater, plantCycle, plantSun)
    }

    private fun showBasicInfo(
        name: String, scientific: String, cycle: String,
        watering: String, sunlight: String, imageUrl: String, edible: Boolean
    ) {
        binding.collapsingToolbar.title = name.replaceFirstChar { it.uppercase() }
        binding.tvDetailName.text       = name.replaceFirstChar { it.uppercase() }
        binding.tvDetailScientific.text = scientific

        binding.tvSciNameCard.text    = scientific.ifBlank { "N/A" }
        binding.tvFamilyCard.text     = "Loading..."
        binding.tvWateringCard.text   = watering.replaceFirstChar { it.uppercase() }.ifBlank { "N/A" }
        binding.tvCycleCard.text      = cycle.replaceFirstChar { it.uppercase() }.ifBlank { "N/A" }
        binding.tvSunlightCard.text   = sunlight.replace("_", " ").replaceFirstChar { it.uppercase() }.ifBlank { "N/A" }
        binding.tvHardinessCard.text  = "Loading..."

        binding.badgeCycle.text    = cycle.replaceFirstChar { it.uppercase() }.ifBlank { "N/A" }
        binding.badgeWatering.text = "💧 ${watering.replaceFirstChar { it.uppercase() }.ifBlank { "N/A" }}"
        binding.badgeEdibleDetail.visibility = if (edible) View.VISIBLE else View.GONE

        binding.tvDescription.text = "Loading plant details..."

        if (imageUrl.isNotBlank()) {
            Glide.with(this)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.placeholder_plant)
                .error(R.drawable.placeholder_plant)
                .into(binding.ivDetailImage)
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

    private fun observeViewModel(
        fallbackName: String, fallbackSci: String, fallbackImage: String,
        fallbackWater: String, fallbackCycle: String, fallbackSun: String
    ) {
        plantViewModel.isLoading.observe(this) { loading ->
            binding.detailProgress.visibility = if (loading) View.VISIBLE else View.GONE
        }

        plantViewModel.plantDetail.observe(this) { detail ->
            if (detail == null) return@observe

            // ── Names ──────────────────────────────────────────────
            val displayName = detail.commonName?.replaceFirstChar { it.uppercase() } ?: fallbackName
            binding.collapsingToolbar.title  = displayName
            binding.tvDetailName.text        = displayName
            binding.tvDetailScientific.text  = detail.scientificName?.firstOrNull() ?: fallbackSci

            // ── Info cards — mapped directly from JSON ─────────────
            binding.tvSciNameCard.text   = detail.scientificName?.firstOrNull() ?: "N/A"
            binding.tvFamilyCard.text    = detail.family ?: "N/A"

            binding.tvWateringCard.text  = detail.watering
                ?.replaceFirstChar { it.uppercase() } ?: "N/A"

            // sunlight is a List<String> in JSON — join them
            binding.tvSunlightCard.text  = detail.sunlight
                ?.joinToString(", ") { it.replaceFirstChar { c -> c.uppercase() } } ?: "N/A"

            binding.tvCycleCard.text     = detail.cycle
                ?.replaceFirstChar { it.uppercase() } ?: "N/A"

            // hardiness is an object {min, max} in JSON
            binding.tvHardinessCard.text =
                if (detail.hardiness?.min != null && detail.hardiness.max != null)
                    "Zone ${detail.hardiness.min} – ${detail.hardiness.max}"
                else "N/A"

            // ── Badges ─────────────────────────────────────────────
            binding.badgeCycle.text    = detail.cycle?.replaceFirstChar { it.uppercase() } ?: "N/A"
            binding.badgeWatering.text = "💧 ${detail.watering?.replaceFirstChar { it.uppercase() } ?: "N/A"}"

            // edible_leaf OR edible_fruit
            binding.badgeEdibleDetail.visibility =
                if (detail.edibleLeaf == true || detail.edibleFruit == true)
                    View.VISIBLE else View.GONE

            // ── Poison warning ─────────────────────────────────────
            // JSON has poisonous_to_humans as boolean (false) not int
            val isPoisonous = when (val raw = detail.poisonousToHumans) {
                is Int     -> raw > 0
                is Boolean -> raw
                else       -> false
            }
            binding.cardWarning.visibility = if (isPoisonous) View.VISIBLE else View.GONE

            // ── Description ────────────────────────────────────────
            binding.tvDescription.text =
                if (!detail.description.isNullOrBlank()) detail.description
                else "No description available for this plant."

            // ── Image — use best quality available ─────────────────
            val imageUrl = detail.defaultImage?.originalUrl
                ?: detail.defaultImage?.regularUrl
                ?: detail.defaultImage?.mediumUrl
                ?: fallbackImage

            if (imageUrl.isNotBlank()) {
                Glide.with(this)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.placeholder_plant)
                    .error(R.drawable.placeholder_plant)
                    .into(binding.ivDetailImage)
            }

            // ── Add to Garden ──────────────────────────────────────
            binding.btnAddGarden.setOnClickListener {
                val gardenPlant = GardenPlant(
                    id            = detail.id,
                    commonName    = detail.commonName ?: fallbackName,
                    scientificName= detail.scientificName?.firstOrNull() ?: "—",
                    imageUrl      = detail.defaultImage?.mediumUrl
                        ?: detail.defaultImage?.originalUrl
                        ?: fallbackImage.ifBlank { null },
                    watering      = detail.watering,
                    cycle         = detail.cycle,
                    sunlight      = detail.sunlight?.firstOrNull()
                )
                gardenViewModel.addToGarden(gardenPlant)
            }
        }

        // ── Garden state ───────────────────────────────────────────
        gardenViewModel.isInGarden.observe(this) { inGarden ->
            binding.btnAddGarden.text      = if (inGarden) "✓ In My Garden" else "🌱 Add to My Garden"
            binding.btnAddGarden.isEnabled = !inGarden
        }

        gardenViewModel.message.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                gardenViewModel.clearMessage()
                gardenViewModel.checkIsInGarden(plantId)
            }
        }

        // ── Errors — silent fallback ───────────────────────────────
        plantViewModel.error.observe(this) { err ->
            err?.let {
                if (binding.tvDescription.text == "Loading plant details...") {
                    binding.tvDescription.text = "Detailed info unavailable for this plant."
                    binding.tvFamilyCard.text  = "N/A"
                    binding.tvHardinessCard.text = "N/A"
                }
                plantViewModel.clearError()
            }
        }
    }
}