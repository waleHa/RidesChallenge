package com.ibm.data.model

import com.google.gson.annotations.SerializedName
import com.ibm.domain.model.VehicleResponse


data class VehicleResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("uid") val uid: String,
    @SerializedName("vin") val vin: String,
    @SerializedName("make_and_model") val makeAndModel: String,
    @SerializedName("color") val color: String,
    @SerializedName("transmission") val transmission: String,
    @SerializedName("drive_type") val driveType: String,
    @SerializedName("fuel_type") val fuelType: String,
    @SerializedName("car_type") val carType: String,
    @SerializedName("doors") val doors: Int,
    @SerializedName("mileage") val mileage: Int,
    @SerializedName("kilometrage") val kilometrage: Int,
    @SerializedName("license_plate") val licensePlate: String
) {
    fun toDomainModel(): VehicleResponse {
        return VehicleResponse(
            vin = vin,
            makeAndModel = makeAndModel,
            color = color,
            carType = carType,
            transmission = transmission,  // Map the new fields
            driveType = driveType,        // Map the new fields
            fuelType = fuelType,          // Map the new fields
            doors = doors,                // Map the new fields
            mileage = mileage,            // Map the new fields
            kilometrage = kilometrage,    // Map the new fields
            licensePlate = licensePlate   // Map the new fields
        )
    }
}


