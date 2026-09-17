package com.findrestaurant.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.findrestaurant.android.data.model.FoodItem

@Composable
fun PopularItemCard(
    modifier: Modifier = Modifier,
    item: FoodItem
) {
    Column(
        modifier = modifier
            .width(130.dp)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_gallery),
            error = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_report_image)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Text(
            text = item.price,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
