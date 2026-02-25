package com.example.herbify.ui.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.herbify.adapter.PlantAdapter
import com.example.herbify.databinding.FragmentExploreBinding
import com.example.herbify.viewmodel.PlantViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlantViewModel by viewModels()
    private lateinit var plantAdapter: PlantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupFilters()
        setupLoadMore()
        observeData()
    }

    private fun setupRecyclerView() {
        plantAdapter = PlantAdapter(emptyList()) { plant ->
            val intent = Intent(requireContext(), PlantDetailActivity::class.java)
            intent.putExtra("plant_id", plant.id)
            intent.putExtra("plant_name", plant.commonName ?: "Plant")
            startActivity(intent)
        }
        binding.rvPlants.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = plantAdapter
        }
    }

    private fun setupSearch() {
        var searchJob: Job? = null
        binding.etSearch.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = MainScope().launch {
                delay(600)
                val query = text?.toString()?.trim()
                viewModel.searchPlants(query ?: "")
            }
        }
    }

    private fun setupFilters() {
        // ✅ Fixed: chip_group_filter → chipGroupFilter (viewBinding camelCase conversion)
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull()
            when (checkedId) {
                binding.chipAll.id      -> viewModel.filterPlants()
                binding.chipEdible.id   -> viewModel.filterPlants(edible = 1)
                binding.chipIndoor.id   -> viewModel.filterPlants(indoor = 1)
                binding.chipPerennial.id -> viewModel.filterPlants(cycle = "perennial")
                binding.chipAnnual.id   -> viewModel.filterPlants(cycle = "annual")
                binding.chipFullSun.id  -> viewModel.filterPlants(sunlight = "full_sun")
            }
        }
    }

    private fun setupLoadMore() {
        binding.btnLoadMore.setOnClickListener {
            viewModel.loadMorePlants()
        }
    }

    private fun observeData() {
        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            plantAdapter.updatePlants(plants)
            binding.emptyState.visibility = if (plants.isEmpty()) View.VISIBLE else View.GONE
            binding.rvPlants.visibility   = if (plants.isEmpty()) View.GONE  else View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.hasMorePages.observe(viewLifecycleOwner) { hasMore ->
            binding.btnLoadMore.visibility = if (hasMore) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}