package com.findrestaurant.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findrestaurant.android.data.model.FoodItem
import com.findrestaurant.android.data.model.Restaurant
import com.findrestaurant.android.data.repository.RestaurantRepository
import com.findrestaurant.android.domain.usecase.GetRestaurantByIdUseCase
import com.findrestaurant.android.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RestaurantDetailUiState(
    val restaurant: Restaurant? = null,
    val popularItems: List<FoodItem> = emptyList(),
    val isLoading: Boolean = true
)

class RestaurantDetailViewModel(
    private val restaurantId: String,
    getRestaurantByIdUseCase: GetRestaurantByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val repository: RestaurantRepository // For popular items
) : ViewModel() {

    val uiState: StateFlow<RestaurantDetailUiState> = getRestaurantByIdUseCase(restaurantId)
        .map { restaurant ->
            RestaurantDetailUiState(
                restaurant = restaurant,
                popularItems = if (restaurant != null) repository.getPopularItems(restaurantId) else emptyList(),
                isLoading = false
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RestaurantDetailUiState())

    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(restaurantId)
        }
    }
}
