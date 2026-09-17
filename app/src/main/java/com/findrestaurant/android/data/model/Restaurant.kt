package com.findrestaurant.android.data.model

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val rating: Float,
    val reviewCount: String, // e.g. "1.2k"
    val deliveryFee: String, // e.g. "R 25"
    val deliveryTime: String, // e.g. "25-35 min"
    val isOpen: Boolean,
    val imageUrl: String,
    val tags: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val closingTime: String = "10:00 PM"
)
