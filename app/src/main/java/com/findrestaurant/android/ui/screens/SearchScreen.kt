package com.findrestaurant.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.findrestaurant.android.ui.components.EmptyView
import com.findrestaurant.android.ui.components.RestaurantCard
import com.findrestaurant.android.ui.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
                Text(
                    text = "Search restaurants",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = { viewModel.updateQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search by name or category") },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFFFF5722)
                ),
                singleLine = true
            )

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
                    onCheckedChange = { viewModel.toggleOpenNow() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFF5722)
                    )
                )
            }

            if (uiState.query.isNotEmpty() && uiState.results.isEmpty()) {
                Box(modifier = Modifier.weight(1f)) {
                    EmptyView(onClear = { viewModel.updateQuery("") })
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = uiState.results,
                        key = { it.id }
                    ) { restaurant ->
                        RestaurantCard(
                            restaurant = restaurant,
                            onClick = { onRestaurantClick(restaurant.id) },
                            onFavoriteClick = { viewModel.toggleFavorite(restaurant.id) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}
