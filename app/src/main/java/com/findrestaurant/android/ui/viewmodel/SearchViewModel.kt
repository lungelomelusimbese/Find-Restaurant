package com.findrestaurant.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.findrestaurant.android.data.model.Restaurant
import com.findrestaurant.android.domain.usecase.GetRestaurantsUseCase
import com.findrestaurant.android.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<Restaurant> = emptyList(),
    val isOpenNowOnly: Boolean = false
)

class SearchViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _isOpenNowOnly = MutableStateFlow(false)

    val uiState: StateFlow<SearchUiState> = combine(
        getRestaurantsUseCase(),
        _query,
        _isOpenNowOnly
    ) { restaurants, query, isOpenNowOnly ->
        val results = if (query.isBlank()) {
            emptyList()
        } else {
            restaurants.filter { 
                (it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)) &&
                (!isOpenNowOnly || it.isOpen)
            }
        }
        SearchUiState(query, results, isOpenNowOnly)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchUiState())

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun toggleOpenNow() {
        _isOpenNowOnly.value = !_isOpenNowOnly.value
    }

    fun toggleFavorite(restaurantId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(restaurantId)
        }
    }
}
