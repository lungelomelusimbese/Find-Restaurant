package com.findrestaurant.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    locationName: String,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth().statusBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Current location", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(text = locationName, style = MaterialTheme.typography.bodyMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Tune, contentDescription = "Filter")
            }
        }

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onSearchClick() }) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search restaurants...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledContainerColor = Color(0xFFF5F5F5),
                    disabledBorderColor = Color.Transparent,
                    disabledPlaceholderColor = Color.Gray,
                    disabledLeadingIconColor = Color.Gray
                ),
                singleLine = true,
                enabled = false
            )
        }
    }
}
