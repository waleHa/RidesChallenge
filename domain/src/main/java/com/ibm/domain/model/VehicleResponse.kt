package com.ibm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class VehicleResponse(
    val vin: String,
    val makeAndModel: String,
    val color: String,
    val carType: String,
    val kilometrage: Int
    // You can add more fields if needed
) : Parcelable


