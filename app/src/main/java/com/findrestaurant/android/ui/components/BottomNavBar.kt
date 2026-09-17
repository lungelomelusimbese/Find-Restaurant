package com.findrestaurant.android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Browse : Screen("browse", "Browse", Icons.Default.Restaurant)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    object Search : Screen("search", "Search", Icons.Default.Restaurant)
    object RestaurantDetail : Screen("detail/{restaurantId}", "Detail", Icons.Default.Restaurant)
}

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        val items = listOf(Screen.Browse, Screen.Favorites)
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFF5722),
                    selectedTextColor = Color(0xFFFF5722),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
