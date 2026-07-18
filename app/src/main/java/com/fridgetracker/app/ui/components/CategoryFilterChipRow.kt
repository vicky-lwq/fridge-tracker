package com.fridgetracker.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.data.FoodCategory

/**
 * Horizontally scrolling filter row for the list screen. This is a deliberate exception
 * to the "no horizontal scroll on mobile" baseline — a filter row is the accepted case.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterChipRow(
    selected: FoodCategory?,
    onSelect: (FoodCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            CategoryChip(label = "全部", isSelected = selected == null, onClick = { onSelect(null) })
        }
        items(FoodCategory.entries.toList()) { category ->
            CategoryChip(
                label = category.displayName,
                isSelected = selected == category,
                onClick = { onSelect(category) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        leadingIcon = if (isSelected) {
            { Icon(Icons.Filled.Check, contentDescription = null) }
        } else {
            null
        },
        shape = MaterialTheme.shapes.small,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = MaterialTheme.colorScheme.outline
        )
    )
}
