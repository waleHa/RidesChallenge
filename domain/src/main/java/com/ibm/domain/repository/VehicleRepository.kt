package com.ibm.domain.repository

import com.ibm.core.Resource
import com.ibm.domain.model.VehicleResponse
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    // Function to fetch a list of vehicles
    suspend fun getVehicles(size: Int): Flow<Resource<List<VehicleResponse>>>
}
