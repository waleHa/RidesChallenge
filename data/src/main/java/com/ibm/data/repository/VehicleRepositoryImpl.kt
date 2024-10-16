package com.ibm.data.repository

import com.ibm.data.remote.VehicleApi
import com.ibm.domain.model.VehicleResponse
import com.ibm.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.ibm.core.common.Resource
import kotlinx.coroutines.flow.flow

class VehicleRepositoryImpl @Inject constructor(
    private val api: VehicleApi
) : VehicleRepository {

    override suspend fun getVehicles(size: Int): Flow<Resource<List<VehicleResponse>>> = flow {
        emit(Resource.Loading())
        try {
            val vehicleDtos = api.getVehicles(size)
            val vehicles = vehicleDtos.map { it.toDomainModel() } // Map DTOs to domain models
            emit(Resource.Success(vehicles))
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching vehicles"))
        }
    }
}
