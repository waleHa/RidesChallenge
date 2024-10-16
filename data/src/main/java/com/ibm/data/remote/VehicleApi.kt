package com.ibm.data.remote

import com.ibm.data.model.VehicleResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface VehicleApi {
    @GET("api/vehicle/random_vehicle")
    suspend fun getVehicles(@Query("size") size: Int): List<VehicleResponseDto> // Adjust the return type to List<VehicleResponseDto>
}

