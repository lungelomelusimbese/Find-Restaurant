package com.findrestaurant.android

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.findrestaurant.android.domain.usecase.SyncRestaurantsUseCase
import com.findrestaurant.android.ui.components.BottomNavBar
import com.findrestaurant.android.ui.components.Screen
import com.findrestaurant.android.ui.screens.*
import com.findrestaurant.android.ui.theme.FindRestaurantTheme
import com.findrestaurant.android.utils.LocationHelper
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FindRestaurantTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val locationHelper = remember { LocationHelper(context) }
                val syncRestaurantsUseCase: SyncRestaurantsUseCase = koinInject()
                
                var locationName by remember { mutableStateOf("Locating...") }
                var isSyncing by remember { mutableStateOf(true) }

                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                        permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
                    ) {
                        scope.launch {
                            locationName = locationHelper.getCurrentLocationName() ?: "Unknown Location"
                        }
                    } else {
                        locationName = "Permission Denied"
                    }
                }

                LaunchedEffect(Unit) {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                    syncRestaurantsUseCase()
                    isSyncing = false
                }

                if (isSyncing) {
                    com.findrestaurant.android.ui.components.LoadingView()
                } else {
                    MainApp(locationName = locationName)
                }
            }
        }
    }
}

@Composable
fun MainApp(locationName: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Browse.route || currentRoute == Screen.Favorites.route) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Browse.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Browse.route) {
                BrowseScreen(
                    locationName = locationName,
                    onRestaurantClick = { id ->
                        navController.navigate("detail/$id")
                    },
                    onSearchClick = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onRestaurantClick = { id ->
                        navController.navigate("detail/$id")
                    }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onBack = { navController.popBackStack() },
                    onRestaurantClick = { id ->
                        navController.navigate("detail/$id")
                    }
                )
            }
            composable(
                route = Screen.RestaurantDetail.route,
                deepLinks = listOf(
                    navDeepLink { uriPattern = "findrestaurant://detail/{restaurantId}" }
                )
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
                RestaurantDetailScreen(
                    restaurantId = restaurantId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
