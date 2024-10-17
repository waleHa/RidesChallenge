package com.ibm.rides.presentation.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibm.core.Resource
import com.ibm.data.di.IoDispatcher
import com.ibm.domain.model.VehicleResponse
import com.ibm.domain.usecase.GetVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val getVehiclesUseCase: GetVehiclesUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _vehiclesState = MutableStateFlow<Resource<List<VehicleResponse>>>(Resource.Loading())
    val vehiclesState: StateFlow<Resource<List<VehicleResponse>>> = _vehiclesState.asStateFlow()

    private var fetchedVehicles: List<VehicleResponse>? = null

    fun fetchVehicles(
        size: Int,
        sortOption: GetVehiclesUseCase.SortOption = GetVehiclesUseCase.SortOption.VinAsc
    ) {
        // Input validation: Ensure size is between 1 and 100
        if (size !in 1..100) {
            _vehiclesState.value = Resource.Error("Input must be between 1 and 100")
            return
        }

        viewModelScope.launch(ioDispatcher) {
            //.onEach handles emissions
            getVehiclesUseCase.execute(GetVehiclesUseCase.Params(size, sortOption))
                .onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            fetchedVehicles = result.data
                            val sortedVehicles = sortVehicles(fetchedVehicles, sortOption)
                            _vehiclesState.value = Resource.Success(sortedVehicles)
                        }
                        is Resource.Error -> {
                            _vehiclesState.value = Resource.Error(result.message ?: "Unknown error")
                        }
                        is Resource.Loading -> {
                            _vehiclesState.value = Resource.Loading()
                        }
                    }
                }
                .catch { e ->
                    _vehiclesState.value = Resource.Error(e.message ?: "Unknown error")
                }
                .launchIn(this) // Use launchIn to collect the flow in the current coroutine scope
        }
    }

    fun setSortOption(newSortOption: GetVehiclesUseCase.SortOption) {
        fetchedVehicles?.let { vehicles ->
            val sortedVehicles = sortVehicles(vehicles, newSortOption)
            _vehiclesState.value = Resource.Success(sortedVehicles)
        }
    }

    private fun sortVehicles(
        vehicles: List<VehicleResponse>?,
        sortOption: GetVehiclesUseCase.SortOption
    ): List<VehicleResponse> {
        return when (sortOption) {
            GetVehiclesUseCase.SortOption.VinAsc -> vehicles?.sortedBy { it.vin } ?: emptyList()
            GetVehiclesUseCase.SortOption.VinDesc -> vehicles?.sortedByDescending { it.vin } ?: emptyList()
            GetVehiclesUseCase.SortOption.MakeAndModelAsc -> vehicles?.sortedBy { it.makeAndModel.trim().lowercase() } ?: emptyList()
            GetVehiclesUseCase.SortOption.MakeAndModelDesc -> vehicles?.sortedByDescending { it.makeAndModel.trim().lowercase() } ?: emptyList()
        }
    }
}
