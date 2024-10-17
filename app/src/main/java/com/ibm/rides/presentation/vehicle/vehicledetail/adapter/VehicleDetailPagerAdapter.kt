package com.ibm.rides.presentation.vehicle.vehicledetail.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ibm.domain.model.VehicleResponse
import com.ibm.rides.presentation.vehicle.vehicledetail.VehicleInfoFragment
import com.ibm.rides.presentation.vehicle.vehicledetail.VehicleEmissionsFragment

class VehicleDetailPagerAdapter(
    activity: AppCompatActivity,
    private val vehicle: VehicleResponse
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2 // Two pages: details and emissions

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VehicleInfoFragment.newInstance(vehicle)
            1 -> VehicleEmissionsFragment.newInstance(vehicle)
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
