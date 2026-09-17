package com.findrestaurant.android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.findrestaurant.android.data.model.Restaurant

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: String,
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

fun RestaurantEntity.toDomain(isFavorite: Boolean): Restaurant {
    return Restaurant(
        id = id,
        name = name,
        description = description,
        category = category,
        rating = rating,
        reviewCount = reviewCount,
        deliveryFee = deliveryFee,
        deliveryTime = deliveryTime,
        isOpen = isOpen,
        imageUrl = imageUrl,
        isFavorite = isFavorite
    )
}

fun Restaurant.toEntity(): RestaurantEntity {
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
