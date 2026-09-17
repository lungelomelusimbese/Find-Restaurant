package com.findrestaurant.android.data.remote

import retrofit2.http.GET

interface RestaurantApi {
    @GET("restaurants")
    suspend fun getRestaurants(): List<RestaurantDto>
}

class MockRestaurantApi : RestaurantApi {
    override suspend fun getRestaurants(): List<RestaurantDto> {
        kotlinx.coroutines.delay(1000) // Simulate network delay
        val categories = listOf("Pizza", "Burgers", "Sushi", "Asian", "Italian", "Mexican", "Desserts", "Steakhouse")
        val suburbs = listOf("Riverside", "Green Point", "Sea Point", "City Bowl", "Gardens", "Claremont")
        val imageIds = listOf(
            "1517248135467-4c7edcad34c4",
            "1552566626-52f8b828add9",
            "1555396273-367ea4eb4db5",
            "1514362545857-3bc16c4c7d1b",
            "1504674900247-0877df9cc836",
            "1414235077428-338989a2e8c0",
            "1551218808-94e220e084d2",
            "1533777857889-4be7c70b33f7"
        )
        
        return (1..200).map { i ->
            val category = categories[i % categories.size]
            val suburb = suburbs[i % suburbs.size]
            val imageId = imageIds[i % imageIds.size]
            RestaurantDto(
                id = i.toString(),
                name = "Restaurant $i ($category)",
                description = "This is a detailed description for Restaurant $i located in $suburb. We offer the best $category in town.",
                category = "$category • Fast Food • Local",
                rating = (30..50).random() / 10f,
                reviewCount = "${(10..2000).random()}",
                deliveryFee = "R ${(10..50).random()} delivery fee",
                deliveryTime = "${(15..60).random()}-${(60..90).random()} min",
                isOpen = (0..1).random() == 1,
                imageUrl = "https://images.unsplash.com/photo-$imageId?q=80&w=500"
            )
        }
    }
}
