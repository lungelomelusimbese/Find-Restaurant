package com.findrestaurant.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findrestaurant.android.data.model.Restaurant
import com.findrestaurant.android.domain.usecase.GetRestaurantsUseCase
import com.findrestaurant.android.domain.usecase.SyncRestaurantsUseCase
import com.findrestaurant.android.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BrowseUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val isOpenNowOnly: Boolean = false,
    val locationName: String = "Locating..."
)

class BrowseViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val syncRestaurantsUseCase: SyncRestaurantsUseCase
) : ViewModel() {

    private val _isOpenNowOnly = MutableStateFlow(false)
    private val _locationName = MutableStateFlow("Locating...")
    private val _isLoading = MutableStateFlow(false)

    val uiState: StateFlow<BrowseUiState> = combine(
        getRestaurantsUseCase(),
        _isOpenNowOnly,
        _locationName,
        _isLoading
    ) { restaurants, isOpenNowOnly, location, isLoading ->
        val filtered = if (isOpenNowOnly) {
            restaurants.filter { it.isOpen }
        } else {
            restaurants
        }
        BrowseUiState(
            restaurants = filtered,
            isLoading = isLoading,
            isOpenNowOnly = isOpenNowOnly,
            locationName = location
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BrowseUiState())

    fun toggleOpenNowFilter() {
        _isOpenNowOnly.value = !_isOpenNowOnly.value
    }

    fun updateLocation(name: String) {
        _locationName.value = name
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(restaurantId)
        }
    }
}
