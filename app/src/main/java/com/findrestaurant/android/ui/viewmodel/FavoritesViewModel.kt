package com.findrestaurant.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findrestaurant.android.data.model.Restaurant
import com.findrestaurant.android.domain.usecase.GetRestaurantsUseCase
import com.findrestaurant.android.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favoriteRestaurants: List<Restaurant> = emptyList(),
    val selectedTab: Int = 0
)

class FavoritesViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)

    val uiState: StateFlow<FavoritesUiState> = combine(
        getRestaurantsUseCase(),
        _selectedTab
    ) { restaurants, selectedTab ->
        FavoritesUiState(
            favoriteRestaurants = restaurants.filter { it.isFavorite },
            selectedTab = selectedTab
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoritesUiState())

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(restaurantId)
        }
    }
}
