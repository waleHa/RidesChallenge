package com.ibm.rides.presentation.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibm.core.Resource
import com.ibm.domain.model.VehicleResponse
import com.ibm.domain.usecase.GetVehiclesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val getVehiclesUseCase: GetVehiclesUseCase
) : ViewModel() {

    private val _vehiclesState = MutableStateFlow<Resource<List<VehicleResponse>>>(Resource.Loading())
    val vehiclesState: StateFlow<Resource<List<VehicleResponse>>> = _vehiclesState

    // Store the fetched vehicles list to apply sorting without refetching
    private var fetchedVehicles: List<VehicleResponse>? = null

    // Fetches vehicles based on the input size and stores them in the ViewModel
    fun fetchVehicles(size: Int, sortOption: GetVehiclesUseCase.SortOption = GetVehiclesUseCase.SortOption.VinAsc) {
        viewModelScope.launch {
            _vehiclesState.value = Resource.Loading() // Show loading state
            getVehiclesUseCase.execute(GetVehiclesUseCase.Params(size, sortOption)).collect { result ->
                if (result is Resource.Success) {
                    // Store the fetched vehicles
                    fetchedVehicles = result.data
                    // Sort the vehicles before passing them to the UI
                    val sortedVehicles = sortVehicles(fetchedVehicles, sortOption)
                    _vehiclesState.value = Resource.Success(sortedVehicles)
                } else {
                    _vehiclesState.value = result // Pass error or loading states
                }
            }
        }
    }

    // Updates the sorting option and applies sorting to the already fetched vehicles
    fun setSortOption(newSortOption: GetVehiclesUseCase.SortOption) {
        fetchedVehicles?.let { vehicles ->
            val sortedVehicles = sortVehicles(vehicles, newSortOption)
            _vehiclesState.value = Resource.Success(sortedVehicles)
        }
    }

    // Sorting logic for the vehicles
    private fun sortVehicles(vehicles: List<VehicleResponse>?, sortOption: GetVehiclesUseCase.SortOption): List<VehicleResponse> {
        return when (sortOption) {
            GetVehiclesUseCase.SortOption.VinAsc -> vehicles?.sortedBy { it.vin } ?: emptyList()
            GetVehiclesUseCase.SortOption.VinDesc -> vehicles?.sortedByDescending { it.vin } ?: emptyList()
            GetVehiclesUseCase.SortOption.MakeAndModelAsc -> vehicles?.sortedBy { it.makeAndModel.trim().lowercase() } ?: emptyList()
            GetVehiclesUseCase.SortOption.MakeAndModelDesc -> vehicles?.sortedByDescending { it.makeAndModel.trim().lowercase() } ?: emptyList()
        }
    }
}
