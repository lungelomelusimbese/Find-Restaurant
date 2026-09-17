package com.findrestaurant.android.di

import androidx.room.Room
import com.findrestaurant.android.data.local.AppDatabase
import com.findrestaurant.android.data.local.FavoritesDataStore
import com.findrestaurant.android.data.remote.MockRestaurantApi
import com.findrestaurant.android.data.remote.RestaurantApi
import com.findrestaurant.android.data.repository.RestaurantRepository
import com.findrestaurant.android.domain.usecase.*
import com.findrestaurant.android.ui.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val appModule = module {
    // Data
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "restaurant_db"
        ).build()
    }
    single { get<AppDatabase>().restaurantDao() }
    single { FavoritesDataStore(androidContext()) }
    single<RestaurantApi> { MockRestaurantApi() }
    single { RestaurantRepository(get(), get(), get()) }

    // Domain
    factory { GetRestaurantsUseCase(get()) }
    factory { GetRestaurantByIdUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { SyncRestaurantsUseCase(get()) }

    // Presentation
    viewModel { BrowseViewModel(get(), get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
    viewModel { (restaurantId: String) ->
        RestaurantDetailViewModel(restaurantId, get(), get(), get())
    }
}
