package com.example.herbify.ui.garden

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.herbify.adapter.GardenAdapter
import com.example.herbify.databinding.FragmentGardenBinding
import com.example.herbify.ui.explore.PlantDetailActivity
import com.example.herbify.viewmodel.GardenViewModel


class GardenFragment : Fragment() {

    private var _binding: FragmentGardenBinding? = null
    private val binding get() = _binding!!
    private val gardenViewModel: GardenViewModel by viewModels()
    private lateinit var gardenAdapter: GardenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGardenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        binding.btnGoExplore?.setOnClickListener {
            activity?.let {
                val navController = (it as? androidx.navigation.NavController)
                // navigate to explore
            }
        }
    }

    override fun onResume() {
        super.onResume()
        gardenViewModel.loadGarden()
    }

    private fun setupRecyclerView() {
        gardenAdapter = GardenAdapter(
            plants = emptyList(),
            onRemoveClick = { plant ->
                gardenViewModel.removeFromGarden(plant)
            },
            onItemClick = { plant ->
                val intent = Intent(requireContext(), PlantDetailActivity::class.java)
                intent.putExtra("plant_id", plant.id)
                intent.putExtra("plant_name", plant.commonName)
                startActivity(intent)
            }
        )

        binding.rvGarden.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gardenAdapter
        }
    }

    private fun setupObservers() {
        gardenViewModel.gardenPlants.observe(viewLifecycleOwner) { plants ->
            gardenAdapter.updatePlants(plants)
            binding.tvPlantCount.text = "${plants.size} plant${if (plants.size != 1) "s" else ""}"

            if (plants.isEmpty()) {
                binding.llEmptyGarden.visibility = View.VISIBLE
                binding.rvGarden.visibility = View.GONE
            } else {
                binding.llEmptyGarden.visibility = View.GONE
                binding.rvGarden.visibility = View.VISIBLE
            }
        }

        gardenViewModel.message.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                gardenViewModel.clearMessage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}