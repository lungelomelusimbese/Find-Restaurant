# Scaling the Find Restaurant Feature

To evolve this feature for a large-scale application maintained by multiple squads, I would transition from a monolithic structure to a robust, multi-module architecture. This document outlines the technical strategy for such a scale-up.

## 1. Modularization and Shared Components
I would adopt a **feature-layered modularization** strategy to enable parallel development and reduce build times.

*   **Feature Modules**: Each high-level flow (Browse, Search, Favorites, Detail) becomes its own module (e.g., `:feature:browse`, `:feature:detail`). Squads "own" these modules.
*   **Design System Module (`:core:designsystem`)**: Contains atomic UI components (Buttons, Typography, `RestaurantCard`). This ensures visual consistency and allows squads to reuse components without duplicating code.
*   **Data Modules (`:core:data`, `:core:network`, `:core:database`)**: Separates data concerns. Feature modules only depend on `:core:data`, which hides the complexity of Retrofit and Room.
*   **Domain Module (`:core:domain`)**: Shared Use Cases and business models.

## 2. Navigation, State, and Data Boundaries
Clear boundaries prevent squads from interfering with each other's work.

*   **Navigation Contracts**: Instead of hardcoded routes, we would use a contract-based approach (e.g., `:core:navigation`). Feature A requests navigation to Feature B via an interface, and the `:app` module (the orchestrator) provides the implementation.
*   **State Isolation**: Each feature manages its own `UiState`. We would favor **MVI (Model-View-Intent)** for complex features to ensure state predictability and easier debugging in multi-squad environments.
*   **Data Boundaries**: We would implement a **Single Source of Truth (SSOT)** using the Room database. If the "Favorites Squad" updates a status, the "Browse Squad" sees it immediately because both observe the same local database stream.

## 3. Testing Strategy
A multi-layered testing approach is essential for large teams:

*   **Unit Tests**: Mandatory for every Use Case and ViewModel. We would use **Turbine** for testing Flow emissions and **MockK** for repository mocking.
*   **Integration Tests**: Use **Hilt/Koin** test modules to verify the interaction between the Repository and Room/Retrofit.

## 4. UI Performance for Large Lists
As the restaurant list grows from 200 to thousands, the following optimizations are required:

*   **Pagination (Jetpack Paging 3)**: Replace the current `List` flow with `Pager`. This loads data in chunks, preventing memory spikes and keeping the UI responsive.
*   **Compose Stability**: Use `@Stable` and `@Immutable` annotations on data models to avoid unnecessary recompositions.
*   **Lazy Layout Optimizations**: Use `contentType` and `key` in `LazyColumn` items to help Compose identify and reuse nodes effectively during fast scrolling.
*   **Image Optimization**: Use a global Coil configuration with custom disk caching and dynamic image resizing (requesting only the resolution required by the UI).

## 5. AI-Assisted Development and Quality
AI should be used as a force multiplier without compromising code quality:

*   **Boilerplate & Tests**: Leverage AI to generate repetitive code (Room entities, DTOs, Unit test stubs).
*   **Consistency Checks**: Use AI to verify that new UI code adheres to the established `:core:designsystem` patterns.
*   **Review Process**: Maintain a strict "Human-in-the-loop" PR process. AI generates code, but squad leads perform architectural reviews to ensure the modular boundaries are respected.
*   **Custom Linting**: Implement custom Lint rules or use AI-driven static analysis to enforce project-specific architectural constraints (e.g., "Feature modules must not depend on other feature modules").

# Tech Stack Trade-offs
Choosing a tech stack is always a balance between development speed, performance, and long-term maintainability. Here are the key trade-offs made for the Find Restaurant app.

## 1. Dependency Injection: Koin vs. Hilt

*   Selected: Koin
*   Trade-off:
    * Pros: We chose Koin for its simplicity and speed. It doesn't rely on code generation (KSP/KAPT), which kept our build times fast during the prototyping phase. Its DSL is very readable and integrates naturally with Jetpack Compose.
    * Cons: Unlike Hilt, Koin resolves dependencies at runtime. This means if a dependency is missing, the app will crash when that screen opens, rather than failing at compile time.
    * Decision: For this project, the reduced boilerplate and faster development cycle outweighed the strict compile-time safety of Hilt.

## 2. Persistence: DataStore vs. Room


* Selected: Both (Hybrid approach)
* Trade-off:
  * DataStore (Preferences): We used this for favorite restaurant IDs because it's lightweight and doesn't require a schema. It's ideal for simple user preferences.
  * Room: We used this for the 200-restaurant cache because it handles large, structured datasets and complex queries (like searching name/category) much more efficiently.
  * Decision: While we could have stored everything in Room, using DataStore for favorites demonstrates a clean separation between cached domain data (Room) and user-specific preferences (DataStore).

## 3. Networking: Retrofit vs. Ktor

* Selected: Retrofit
* Trade-off:
  * Pros: Retrofit is the industry standard. Its annotation-based approach is highly declarative, and the ecosystem of converters (Gson) is extremely mature.
  * Cons: Retrofit is a Java-first library. Ktor would have been a better choice if we were planning to transition the project to Kotlin Multiplatform (KMP) in the future.
  * Decision: Given the Android-centric requirements, Retrofit provided the most robust and familiar platform.

## 4. Architecture: MVVM + Use Cases vs. Simple MVVM

* Selected: Use Cases (Domain Layer)
* Trade-off:
  * Pros: Adding Use Cases ensures that Business Logic is decoupled from the UI. If we want to change how "Open Now" is calculated (e.g., adding holiday hours), we only change one class, and both the BrowseScreen and SearchScreen benefit.
  * Cons: It introduces "Class Bloat." For very simple tasks (like just getting a list), it feels like extra boilerplate.
  * Decision: We prioritized Scalability. Use Cases make it much easier for different squads to work on the same data without duplicating logic.

## 5. Reactive Streams: Flow vs. LiveData

* Selected: Flow
* Trade-off:
  * Pros: Flow is built into Kotlin and is much more powerful than LiveData. It supports complex operators (like combine used for merging Room + DataStore) and handles backpressure elegantly.
  * Cons: Requires careful lifecycle management in the UI (e.g., using collectAsStateWithLifecycle) to avoid collecting data while the app is in the background.
  * Decision: Since we are using Jetpack Compose, Flow is the modern and recommended standard for state management.