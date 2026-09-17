package com.findrestaurant.android.domain.usecase

import com.findrestaurant.android.data.repository.RestaurantRepository

class SyncRestaurantsUseCase(private val repository: RestaurantRepository) {
    suspend operator fun invoke() {
        repository.sync()
    }
}
