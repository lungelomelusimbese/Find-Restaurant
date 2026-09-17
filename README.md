# Find Restaurant App

A modern, offline-first Android application for browsing restaurants, built with Jetpack Compose and following Clean Architecture principles.

## Features

- **Location-Aware**: Automatically detects the user's current location on first launch (with permission).
- **Restaurant Browsing**: Browse a list of 200 mock restaurants with realistic details.
- **Offline-First**: Uses Room database to cache data fetched from a simulated network.
- **Deep Linking**: Open specific restaurant details directly via `findrestaurant://detail/{id}`.
- **Favorites**: Persist your favorite restaurants using Jetpack DataStore.
- **Search**: Fully functional search by restaurant name or category.
- **Modern UI**: Built entirely with Jetpack Compose using Material 3.

## Tech Stack

- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Use Cases (Clean Architecture)
- **Dependency Injection**: Koin
- **Networking**: Retrofit (Mocked)
- **Local Database**: Room
- **Persistence**: Preferences DataStore
- **Image Loading**: Coil
- **Concurrency**: Kotlin Coroutines & Flow

## Architecture Overview

The app is organized into a layered architecture:

1.  **Data Layer**: 
    - `RestaurantRepository`: The single source of truth that coordinates between the `MockRestaurantApi` and `Room` database.
    - `FavoritesDataStore`: Manages persistent user preferences.
2.  **Domain Layer**: 
    - Pure business logic encapsulated in **Use Cases** (e.g., `GetRestaurantsUseCase`, `ToggleFavoriteUseCase`).
3.  **Presentation Layer**: 
    - **ViewModels**: Manage screen-specific state and handle user intents.
    - **UI States**: Data classes representing the immutable state of each screen.
    - **Compose Screens**: Declarative UI that observes state changes.

## Deep Linking

You can test the deep link functionality using the following ADB command:

```bash
adb shell am start -W -a android.intent.action.VIEW -d "findrestaurant://detail/10" com.findrestaurant.android
```

## Installation & Setup

1.  Clone the repository.
2.  Open in **Android Studio Ladybug** or newer.
3.  Build and run the `:app` module.
