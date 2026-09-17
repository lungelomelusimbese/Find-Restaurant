package com.findrestaurant.android.data.remote

import com.findrestaurant.android.data.local.RestaurantEntity

data class RestaurantDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val rating: Float,
    val reviewCount: String,
    val deliveryFee: String,
    val deliveryTime: String,
    val isOpen: Boolean,
    val imageUrl: String
)

fun RestaurantDto.toEntity(): RestaurantEntity {
    return RestaurantEntity(
        id = id,
        name = name,
        description = description,
        category = category,
        rating = rating,
        reviewCount = reviewCount,
        deliveryFee = deliveryFee,
        deliveryTime = deliveryTime,
        isOpen = isOpen,
        imageUrl = imageUrl
    )
}
