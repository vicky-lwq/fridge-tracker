package com.fridgetracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.data.FoodCategory

/**
 * Drawer content replacing CategoryFilterChipRow's role on the list screen: category
 * browsing/filtering moves off the list header into a side drawer. Photo assets for the
 * 8 categories aren't ready yet — the same outline icons FoodItemCard uses (categoryIcon())
 * stand in for them until real photos are wired up.
 */
@Composable
fun CategoryDrawerContent(
    selectedCategory: FoodCategory?,
    onSelectCategory: (FoodCategory?) -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Text(
            text = "分类",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp)
        )

        DrawerCategoryRow(
            label = "全部",
            icon = Icons.Outlined.GridView,
            isSelected = selectedCategory == null,
            onClick = {
                onSelectCategory(null)
                onItemClick()
            }
        )

        FoodCategory.entries.forEach { category ->
            DrawerCategoryRow(
                label = category.displayName,
                icon = categoryIcon(category),
                isSelected = selectedCategory == category,
                onClick = {
                    onSelectCategory(category)
                    onItemClick()
                }
            )
        }
    }
}

@Composable
private fun DrawerCategoryRow(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(MaterialTheme.shapes.small)
            .background(
                if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
