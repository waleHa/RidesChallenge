package com.ibm.utils

object EmissionsCalculator {

    /**
     * Calculate carbon emissions based on the kilometrage rules:
     * - 1 unit per km for the first 5,000 km
     * - 1.5 units per km after 5,000 km
     *
     * @param kilometrage The total kilometers traveled
     * @return The total carbon emissions
     */
    fun calculateCarbonEmissions(kilometrage: Int): Double {
        val km = kilometrage.coerceAtLeast(0) // Ensure non-negative kilometrage
        return if (km <= 5000) {
            km.toDouble() // 1 unit per km for first 5000 km
        } else {
            5000 + (km - 5000) * 1.5 // 1.5 units per km after 5000 km
        }
    }
}
