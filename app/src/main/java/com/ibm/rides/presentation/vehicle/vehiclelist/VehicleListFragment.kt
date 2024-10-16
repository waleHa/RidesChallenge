package com.ibm.rides.ui.vehiclelist

import VehicleAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibm.domain.model.VehicleResponse
import com.ibm.rides.R
import com.ibm.core.common.Resource
import com.ibm.rides.databinding.FragmentVehicleListBinding
import com.ibm.rides.ui.VehicleListViewModel
import com.ibm.rides.ui.vehicledetail.VehicleDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleListFragment : Fragment() {

    private var _binding: FragmentVehicleListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VehicleListViewModel by viewModels()
    private lateinit var vehicleAdapter: VehicleAdapter

    private var defaultFetchJob: Job? = null
    private val defaultFetchDelay: Long = 5000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        startDelayedDefaultFetch()

        binding.fetchButton.setOnClickListener {
            val size = binding.vehicleSizeInputEditText.text.toString().toIntOrNull()
            if (size != null && size in 1..100) {
                viewModel.fetchVehicles(size)
            } else {
                Toast.makeText(requireContext(), "Enter a valid number (1-100)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.retryButton.setOnClickListener {
            val size = binding.vehicleSizeInputEditText.text.toString().toIntOrNull()
            size?.let { viewModel.fetchVehicles(it) }
        }

        handleInputChanges()
    }

    private fun setupRecyclerView() {
        vehicleAdapter = VehicleAdapter(::onVehicleClicked)
        binding.vehicleListRecyclerView.apply {
            adapter = vehicleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.vehiclesState.collect { state ->
                when (state) {
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> showVehicles(state.data ?: emptyList())
                    is Resource.Error -> showError(state.message ?: "Unknown Error")
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.vehicleListRecyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showVehicles(vehicles: List<VehicleResponse>) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.vehicleListRecyclerView.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
        vehicleAdapter.submitList(vehicles)
    }

    private fun showError(message: String) {
        binding.loadingProgressBar.visibility = View.GONE
        binding.vehicleListRecyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.errorMessageTextView.text = message
    }

    private fun onVehicleClicked(vehicle: VehicleResponse) {
        val fragment = VehicleDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("vehicle", vehicle)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun startDelayedDefaultFetch() {
        defaultFetchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(defaultFetchDelay)
            val input = binding.vehicleSizeInputEditText.text.toString().toIntOrNull()
            if (input == null) {
                viewModel.fetchVehicles(VehicleListViewModel.DEFAULT_VEHICLE_SIZE)
            }
        }
    }

    private fun handleInputChanges() {
        binding.vehicleSizeInputEditText.addTextChangedListener {
            defaultFetchJob?.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        defaultFetchJob?.cancel()
    }
}
