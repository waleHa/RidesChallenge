package com.ibm.domain.usecase

import com.ibm.core.common.BaseUseCase
import com.ibm.core.common.Resource
import com.ibm.domain.model.VehicleResponse
import com.ibm.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetVehiclesUseCase @Inject constructor(
    private val repository: VehicleRepository
) : BaseUseCase<GetVehiclesUseCase.Params, List<VehicleResponse>>() {

    // Parameters class that contains the size of vehicles to fetch
    data class Params(val size: Int)

    // Executes the use case with the provided Params, calling the repository
    override suspend fun execute(params: Params): Flow<Resource<List<VehicleResponse>>> {
        return repository.getVehicles(params.size)
    }
}

