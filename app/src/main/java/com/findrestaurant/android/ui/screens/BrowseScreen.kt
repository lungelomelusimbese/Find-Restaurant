package com.findrestaurant.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.findrestaurant.android.ui.components.RestaurantCard
import com.findrestaurant.android.ui.components.SearchBox
import com.findrestaurant.android.ui.viewmodel.BrowseViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BrowseScreen(
    locationName: String,
    onRestaurantClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: BrowseViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(locationName) {
        viewModel.updateLocation(locationName)
    }

    Scaffold(
        topBar = {
            SearchBox(
                locationName = uiState.locationName,
                query = "",
                onQueryChange = { },
                onSearchClick = onSearchClick
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Open now", fontWeight = FontWeight.Medium)
                Switch(
                    checked = uiState.isOpenNowOnly,
                    onCheckedChange = { viewModel.toggleOpenNowFilter() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFF5722)
                    )
                )
            }

            Text(
                text = "Nearby restaurants",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Text(
                text = "${uiState.restaurants.size} results",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyColumn {
                items(uiState.restaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(restaurant.id) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
                }
            }
        }
    }
}
