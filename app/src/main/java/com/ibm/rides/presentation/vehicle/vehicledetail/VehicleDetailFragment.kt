package com.ibm.rides.presentation.vehicle.vehicledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ibm.domain.model.VehicleResponse
import com.ibm.rides.databinding.FragmentVehicleDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleDetailFragment : Fragment() {

    private var _binding: FragmentVehicleDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the toolbar title to "Vehicle Details" and enable the back button
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show back button
            supportActionBar?.title = "Vehicle Details"
        }

        setHasOptionsMenu(true) // Required to enable back button functionality in fragment

        // Get the passed Vehicle object from the arguments
        val vehicle = arguments?.getParcelable<VehicleResponse>("vehicle")
        vehicle?.let {
            populateVehicleDetails(it)
        } ?: run {
            binding.vehicleVinTextView.text = "Vehicle details not found"
        }
    }

    private fun populateVehicleDetails(vehicle: VehicleResponse) {
        binding.vehicleVinTextView.text = "VIN: ${vehicle.vin}"
        binding.vehicleMakeModelTextView.text = "Model: ${vehicle.makeAndModel}"
        binding.vehicleColorTextView.text = "Color: ${vehicle.color}"
        binding.vehicleCarTypeTextView.text = "Car Type: ${vehicle.carType}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click in the toolbar
                requireActivity().onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
