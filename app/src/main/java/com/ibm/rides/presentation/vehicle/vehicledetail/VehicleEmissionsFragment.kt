package com.ibm.rides.presentation.vehicle.vehicledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibm.domain.model.VehicleResponse
import com.ibm.rides.databinding.FragmentVehicleEmissionsBinding
import com.ibm.utils.EmissionsCalculator




class VehicleEmissionsFragment : Fragment() {

    private var _binding: FragmentVehicleEmissionsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val VEHICLE_KEY = "vehicle"

        fun newInstance(vehicle: VehicleResponse): VehicleEmissionsFragment {
            val fragment = VehicleEmissionsFragment()
            val args = Bundle()
            args.putParcelable(VEHICLE_KEY, vehicle)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehicleEmissionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vehicle = arguments?.getParcelable<VehicleResponse>(VEHICLE_KEY)
        vehicle?.let {
            val kilometrage = it.kilometrage
            val totalEmissions = EmissionsCalculator.calculateCarbonEmissions(kilometrage)

            binding.vehicleKilometrageTextView.text = "Kilometrage: $kilometrage km"
            binding.vehicleEmissionsTextView.text = "Total Emissions: $totalEmissions units"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
