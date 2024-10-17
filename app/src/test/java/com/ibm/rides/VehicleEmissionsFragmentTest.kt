package com.ibm.rides

import com.ibm.rides.presentation.vehicle.vehicledetail.VehicleEmissionsFragment
import com.ibm.utils.EmissionsCalculator.calculateCarbonEmissions
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class VehicleEmissionsFragmentTest {

    private lateinit var vehicleEmissionsFragment: VehicleEmissionsFragment

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        vehicleEmissionsFragment = VehicleEmissionsFragment()
    }

    @Test
    fun `calculate emissions for kilometrage less than or equal to 5000`() {
        val kilometrage = 4000
        val expectedEmissions = 4000.0
        val actualEmissions = calculateCarbonEmissions(kilometrage)
        assertEquals(expectedEmissions, actualEmissions, 0.0)
    }

    @Test
    fun `calculate emissions for exactly 5000 kilometrage`() {
        val kilometrage = 5000
        val expectedEmissions = 5000.0
        val actualEmissions = calculateCarbonEmissions(kilometrage)
        assertEquals(expectedEmissions, actualEmissions, 0.0)
    }

    @Test
    fun `calculate emissions for kilometrage greater than 5000`() {
        val kilometrage = 8000
        // First 5000 km -> 5000 units
        // Remaining 3000 km -> 3000 * 1.5 = 4500 units
        val expectedEmissions = 5000 + (3000 * 1.5)
        val actualEmissions = calculateCarbonEmissions(kilometrage)
        assertEquals(expectedEmissions, actualEmissions, 0.0)
    }

    @Test
    fun `calculate emissions for exactly 10000 kilometrage`() {
        val kilometrage = 10000
        // First 5000 km -> 5000 units
        // Remaining 5000 km -> 5000 * 1.5 = 7500 units
        val expectedEmissions = 5000 + (5000 * 1.5)
        val actualEmissions = calculateCarbonEmissions(kilometrage)
        assertEquals(expectedEmissions, actualEmissions, 0.0)
    }

    @Test
    fun `calculate emissions for zero kilometrage`() {
        val kilometrage = 0
        val expectedEmissions = 0.0
        val actualEmissions = calculateCarbonEmissions(kilometrage)
        assertEquals(expectedEmissions, actualEmissions, 0.0)
    }
}
