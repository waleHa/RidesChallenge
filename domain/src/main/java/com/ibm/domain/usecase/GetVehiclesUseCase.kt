package com.ibm.domain.usecase

import android.util.Log
import com.ibm.core.BaseUseCase
import com.ibm.core.Resource
import com.ibm.domain.model.VehicleResponse
import com.ibm.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) : BaseUseCase<GetVehiclesUseCase.Params, List<VehicleResponse>>() {

    data class Params(val size: Int, val sortOption: SortOption = SortOption.VinAsc)

    // Sort options for vehicles
    enum class SortOption {
        VinAsc, VinDesc, MakeAndModelAsc, MakeAndModelDesc
    }

    override suspend fun execute(params: Params): Flow<Resource<List<VehicleResponse>>> {
        return repository.getVehicles(params.size).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val sortedVehicles = sortVehicles(resource.data, params.sortOption)
                    Resource.Success(sortedVehicles)
                }
                else -> resource
            }
        }
    }

    private fun sortVehicles(vehicles: List<VehicleResponse>?, sortOption: SortOption): List<VehicleResponse> {
        return when (sortOption) {
            SortOption.VinAsc -> vehicles?.sortedBy { it.vin } ?: emptyList()
            SortOption.VinDesc -> vehicles?.sortedByDescending { it.vin } ?: emptyList()
            SortOption.MakeAndModelAsc -> vehicles?.sortedBy { it.makeAndModel.trim().lowercase() } ?: emptyList() // Trim and lowercase
            SortOption.MakeAndModelDesc -> vehicles?.sortedByDescending { it.makeAndModel.trim().lowercase() } ?: emptyList() // Trim and lowercase
        }.also {
            // Log the sorted list for debugging
            Log.d("SortingVehicles", "Sorted list: ${it.map { v -> v.makeAndModel }}")
        }
    }
}
