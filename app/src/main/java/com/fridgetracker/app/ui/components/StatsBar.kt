package com.fridgetracker.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.data.ExpiryStatus
import com.fridgetracker.app.data.FoodItem
import com.fridgetracker.app.ui.theme.extendedColors
import java.time.LocalDate

/**
 * List-screen stats bar: total / soon-to-expire / expired counts. Always reflects the
 * full food list (not the drawer's category filter) — a global "at a glance" summary,
 * per the "全局视角" decision. Reuses ExpiryBadge's three-state semantic colors so the
 * same color always means the same thing across the screen.
 */
@Composable
fun StatsBar(items: List<FoodItem>, modifier: Modifier = Modifier) {
    val today = LocalDate.now()
    val total = items.size
    val soonCount = items.count { ExpiryStatus.from(it.remainingDays(today)) == ExpiryStatus.SOON }
    val expiredCount = items.count { ExpiryStatus.from(it.remainingDays(today)) == ExpiryStatus.EXPIRED }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            StatColumn(
                value = total,
                label = "食材总数",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            StatColumn(
                value = soonCount,
                label = "即将过期",
                color = MaterialTheme.extendedColors.warning,
                modifier = Modifier.weight(1f)
            )
            StatColumn(
                value = expiredCount,
                label = "已过期",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatColumn(value: Int, label: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = color,
            maxLines = 1
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}
