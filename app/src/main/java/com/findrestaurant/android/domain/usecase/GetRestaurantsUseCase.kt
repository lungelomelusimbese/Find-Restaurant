package com.findrestaurant.android.domain.usecase

import com.findrestaurant.android.data.model.Restaurant
import com.findrestaurant.android.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetRestaurantsUseCase(private val repository: RestaurantRepository) {
    operator fun invoke(): Flow<List<Restaurant>> {
        return repository.getRestaurants()
    }
}
