package com.findrestaurant.android.domain.usecase

import com.findrestaurant.android.data.model.Restaurant
import com.findrestaurant.android.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetRestaurantByIdUseCase(private val repository: RestaurantRepository) {
    operator fun invoke(id: String): Flow<Restaurant?> {
        return repository.getRestaurantById(id)
    }
}
