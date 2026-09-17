package com.findrestaurant.android.data.repository

import com.findrestaurant.android.data.local.FavoritesDataStore
import com.findrestaurant.android.data.local.RestaurantDao
import com.findrestaurant.android.data.local.toDomain
import com.findrestaurant.android.data.model.FoodItem
import com.findrestaurant.android.data.model.Restaurant
import com.findrestaurant.android.data.remote.RestaurantApi
import com.findrestaurant.android.data.remote.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class RestaurantRepository(
    private val restaurantDao: RestaurantDao,
    private val restaurantApi: RestaurantApi,
    private val favoritesDataStore: FavoritesDataStore
) {
    fun getRestaurants(): Flow<List<Restaurant>> {
        return restaurantDao.getAllRestaurants().combine(favoritesDataStore.favoriteRestaurantIds) { entities, favoriteIds ->
            entities.map { it.toDomain(favoriteIds.contains(it.id)) }
        }
    }

    fun getRestaurantById(id: String): Flow<Restaurant?> {
        return restaurantDao.getRestaurantById(id).combine(favoritesDataStore.favoriteRestaurantIds) { entity, favoriteIds ->
            entity?.toDomain(favoriteIds.contains(entity.id))
        }
    }

    suspend fun sync() {
        try {
            val dtos = restaurantApi.getRestaurants()
            val entities = dtos.map { it.toEntity() }
            restaurantDao.insertRestaurants(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun toggleFavorite(restaurantId: String) {
        favoritesDataStore.toggleFavorite(restaurantId)
    }

    fun getPopularItems(restaurantId: String) = listOf(
        FoodItem("1", "Margherita Pizza", "R 89", "https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?q=80&w=300"),
        FoodItem("2", "Pasta Alfredo", "R 79", "https://images.unsplash.com/photo-1645112481338-316223724bc8?q=80&w=300"),
        FoodItem("3", "Tiramisu", "R 55", "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?q=80&w=300"),
        FoodItem("4", "Classic Burger", "R 79", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=300"),
        FoodItem("5", "Truffle Fries", "R 49", "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?q=80&w=300"),
        FoodItem("6", "Milkshake", "R 39", "https://images.unsplash.com/photo-1572490122747-3968b75cc699?q=80&w=300")
    )
}
