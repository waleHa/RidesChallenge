package com.ibm.rides.presentation.vehicle.vehicledetail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ibm.domain.model.VehicleResponse
import com.ibm.rides.databinding.FragmentVehicleInfoBinding


class VehicleInfoFragment : Fragment() {

    private var _binding: FragmentVehicleInfoBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val VEHICLE_KEY = "vehicle"

        fun newInstance(vehicle: VehicleResponse): VehicleInfoFragment {
            val fragment = VehicleInfoFragment()
            val args = Bundle()
            args.putParcelable(VEHICLE_KEY, vehicle)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehicleInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vehicle = arguments?.getParcelable<VehicleResponse>(VEHICLE_KEY)
        vehicle?.let {
            binding.vehicleVinTextView.text = "VIN: ${it.vin}"
            binding.vehicleMakeModelTextView.text = "Model: ${it.makeAndModel}"
            binding.vehicleColorTextView.text = "Color: ${it.color}"
            binding.vehicleCarTypeTextView.text = "Car Type: ${it.carType}"
            binding.vehicleTransmissionTextView.text = "Transmission: ${it.transmission}"
            binding.vehicleDriveTypeTextView.text = "Drive Type: ${it.driveType}"
            binding.vehicleFuelTypeTextView.text = "Fuel Type: ${it.fuelType}"
            binding.vehicleDoorsTextView.text = "Doors: ${it.doors}"
            binding.vehicleMileageTextView.text = "Mileage: ${it.mileage} km"
            binding.vehicleLicensePlateTextView.text = "License Plate: ${it.licensePlate}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
