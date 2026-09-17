package com.findrestaurant.android.domain.usecase

import com.findrestaurant.android.data.repository.RestaurantRepository

class ToggleFavoriteUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke(restaurantId: String) {
        repository.toggleFavorite(restaurantId)
    }
}
