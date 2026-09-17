package com.findrestaurant.android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import java.util.Locale

class LocationHelper(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocationName(): String? {
        return try {
            val location: Location? = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await()

            location?.let { loc ->
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val suburb = address.subLocality ?: address.locality ?: ""
                    val city = address.adminArea ?: address.countryName ?: ""
                    if (suburb.isNotEmpty() && city.isNotEmpty()) {
                        "$suburb, $city"
                    } else if (suburb.isNotEmpty()) {
                        suburb
                    } else {
                        city
                    }
                } else {
                    "Unknown Location"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
