package com.ibm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class VehicleResponse(
    val vin: String,
    val makeAndModel: String,
    val color: String,
    val carType: String,
    val transmission: String,     // New Field
    val driveType: String,        // New Field
    val fuelType: String,         // New Field
    val doors: Int,               // New Field
    val mileage: Int,             // New Field
    val kilometrage: Int,         // New Field
    val licensePlate: String      // New Field
) : Parcelable



