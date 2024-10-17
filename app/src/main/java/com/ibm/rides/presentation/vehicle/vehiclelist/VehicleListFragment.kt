package com.ibm.rides.presentation.vehicle.vehiclelist

import VehicleAdapter
import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibm.core.Resource
import com.ibm.domain.model.VehicleResponse
import com.ibm.domain.usecase.GetVehiclesUseCase
import com.ibm.rides.databinding.FragmentVehicleListBinding
import com.ibm.rides.presentation.vehicle.VehicleViewModel
import com.ibm.rides.presentation.vehicle.vehicledetail.VehicleDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehicleListFragment : Fragment() {

    private var _binding: FragmentVehicleListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VehicleViewModel by viewModels()
    private lateinit var vehicleAdapter: VehicleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(false) // Show back button
            supportActionBar?.title = "Vehicle List"
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        binding.loadingProgressBar.visibility = View.GONE

        setupSortOptions()

        // Fetch happens only on button press
        binding.fetchButton.setOnClickListener {
            val size = binding.vehicleSizeInputEditText.text.toString().toIntOrNull()
            if (size != null && size in 1..100) {
                hideKeyboard() // Hide keyboard when button is pressed
                showLoading() // Show the loading progress only when the button is pressed

                // Get the current sort option from the dropdown menu
                val selectedSortOption = when (binding.sortOptionsSpinner.selectedItemPosition) {
                    0 -> GetVehiclesUseCase.SortOption.VinAsc
                    1 -> GetVehiclesUseCase.SortOption.VinDesc
                    2 -> GetVehiclesUseCase.SortOption.MakeAndModelAsc
                    3 -> GetVehiclesUseCase.SortOption.MakeAndModelDesc
                    else -> GetVehiclesUseCase.SortOption.VinAsc
                }

                // Fetch vehicles with the selected sort option
                viewModel.fetchVehicles(size, selectedSortOption)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid number (1-100)", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupRecyclerView() {
        vehicleAdapter = VehicleAdapter(::onVehicleClicked)
        binding.vehicleListRecyclerView.apply {
            adapter = vehicleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun onVehicleClicked(vehicle: VehicleResponse) {
        val fragment = VehicleDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("vehicle", vehicle)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(com.ibm.rides.R.id.main_nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.vehiclesState.collect { state ->
                when (state) {
                    is Resource.Loading -> showLoading() // This gets triggered only during the fetch
                    is Resource.Success -> showVehicles(state.data ?: emptyList())
                    is Resource.Error -> showError(state.message ?: "Unknown Error")
                }
            }
        }
    }

    private fun setupSortOptions() {
        // Setup a dropdown with sort options
        val sortOptions = arrayOf("VIN Ascending", "VIN Descending", "Car Type Ascending", "Car Type Descending")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, sortOptions)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.sortOptionsSpinner.adapter = adapter

        // Handle selection of sort options
        binding.sortOptionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSortOption = when (position) {
                    0 -> GetVehiclesUseCase.SortOption.VinAsc
                    1 -> GetVehiclesUseCase.SortOption.VinDesc
                    2 -> GetVehiclesUseCase.SortOption.MakeAndModelAsc
                    3 -> GetVehiclesUseCase.SortOption.MakeAndModelDesc
                    else -> GetVehiclesUseCase.SortOption.VinAsc
                }
                // Only change the sorting of the already fetched list, no new fetch
                viewModel.setSortOption(selectedSortOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing if no selection
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

    // Helper function to hide the keyboard
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
