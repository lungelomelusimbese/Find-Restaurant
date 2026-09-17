package com.findrestaurant.android.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites")

class FavoritesDataStore(private val context: Context) {
    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_restaurant_ids")

    val favoriteRestaurantIds: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[FAVORITES_KEY] ?: emptySet()
        }

    suspend fun toggleFavorite(restaurantId: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITES_KEY] ?: emptySet()
            val newFavorites = if (currentFavorites.contains(restaurantId)) {
                currentFavorites - restaurantId
            } else {
                currentFavorites + restaurantId
            }
            preferences[FAVORITES_KEY] = newFavorites
        }
    }
}
