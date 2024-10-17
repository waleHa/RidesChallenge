package com.ibm.rides.presentation.vehicle.vehicledetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.ibm.domain.model.VehicleResponse
import com.ibm.rides.databinding.FragmentVehicleDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


import androidx.viewpager2.widget.ViewPager2
import com.ibm.rides.presentation.vehicle.vehicledetail.adapter.VehicleDetailPagerAdapter

@AndroidEntryPoint
class VehicleDetailFragment : Fragment() {

    private var _binding: FragmentVehicleDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var vehicleDetailPagerAdapter: VehicleDetailPagerAdapter
    private lateinit var viewPager: ViewPager2

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
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Vehicle Details"
        }

        setHasOptionsMenu(true)

        // Initialize the ViewPager and Adapter
        viewPager = binding.viewPager
        val vehicle = arguments?.getParcelable<VehicleResponse>("vehicle")

        vehicle?.let {
            vehicleDetailPagerAdapter = VehicleDetailPagerAdapter(requireActivity() as AppCompatActivity, it)
            viewPager.adapter = vehicleDetailPagerAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
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
