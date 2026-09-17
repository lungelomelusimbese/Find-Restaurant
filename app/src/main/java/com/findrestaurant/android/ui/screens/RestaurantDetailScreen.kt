package com.findrestaurant.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.findrestaurant.android.ui.components.PopularItemCard
import com.findrestaurant.android.ui.components.LoadingView
import com.findrestaurant.android.ui.viewmodel.RestaurantDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    onBack: () -> Unit,
    viewModel: RestaurantDetailViewModel = koinViewModel { parametersOf(restaurantId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val restaurant = uiState.restaurant

    if (uiState.isLoading) {
        LoadingView()
        return
    }

    if (restaurant == null) return

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                AsyncImage(
                    model = restaurant.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_gallery),
                    error = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_report_image)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick = { viewModel.toggleFavorite() },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (restaurant.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (restaurant.isFavorite) Color(0xFFFF5722) else Color.Gray
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = restaurant.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(20.dp))
                        Text(
                            text = "${restaurant.rating} (${restaurant.reviewCount})",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Text(
                            text = restaurant.deliveryTime,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DeliveryDining, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                            Text(
                                text = restaurant.deliveryFee,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        Text(
                            text = if (restaurant.isOpen) "Open" else "Closed",
                            color = if (restaurant.isOpen) Color(0xFF2E7D32) else Color(0xFFC62828),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = restaurant.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Popular items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    LazyRow(
                        contentPadding = PaddingValues(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.popularItems) { item ->
                            PopularItemCard(item = item)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(100.dp)) // Space for the bottom button
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (restaurant.isOpen) Color(0xFFFF5722) else Color.LightGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (restaurant.isOpen) "Add to order" else "Unavailable (Closed)")
            }
        }
    }
}
