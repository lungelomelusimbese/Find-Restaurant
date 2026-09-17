package com.findrestaurant.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.findrestaurant.android.ui.components.RestaurantCard
import com.findrestaurant.android.ui.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    onRestaurantClick: (String) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs = listOf("Restaurants", "Dishes")

    Scaffold(
        topBar = {
            Column {
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    containerColor = Color.White,
                    contentColor = Color(0xFFFF5722),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                            color = Color(0xFFFF5722)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = uiState.selectedTab == index,
                            onClick = { viewModel.selectTab(index) },
                            text = { 
                                Text(
                                    text = title,
                                    color = if (uiState.selectedTab == index) Color(0xFFFF5722) else Color.Gray
                                ) 
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (uiState.selectedTab == 0) {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(uiState.favoriteRestaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(restaurant.id) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = "No favorite dishes yet", color = Color.Gray)
            }
        }
    }
}
