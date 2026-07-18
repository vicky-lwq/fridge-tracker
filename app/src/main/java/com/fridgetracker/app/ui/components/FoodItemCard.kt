package com.fridgetracker.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.EggAlt
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.data.FoodCategory
import com.fridgetracker.app.data.FoodItem
import kotlinx.coroutines.delay

private const val NAME_TRUNCATE_LENGTH = 12

/**
 * Swipe-to-delete list card. Swiping past the threshold always bounces the row back to
 * its settled position first (`confirmValueChange` never returns true), then shows an
 * AlertDialog for confirmation. Only on "删除" does the 200ms shrink+fade exit play,
 * followed by `onDelete`. This two-phase order avoids racing SwipeToDismissBoxState's
 * suspend `reset()` against the dialog's own dismiss timing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItemCard(
    item: FoodItem,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRemoved by remember(item.id) { mutableStateOf(false) }
    var showDeleteConfirm by remember(item.id) { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd || value == SwipeToDismissBoxValue.EndToStart) {
                showDeleteConfirm = true
            }
            false
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(200)
            onDelete()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
    ) {
        SwipeToDismissBox(
            state = dismissState,
            modifier = modifier,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        ) {
            Card(
                onClick = onClick,
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon(item.category),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        item.category?.let { category ->
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    ExpiryBadge(remainingDays = item.remainingDays())
                }
            }
        }
    }

    if (showDeleteConfirm) {
        val truncatedName = if (item.name.length > NAME_TRUNCATE_LENGTH) {
            item.name.take(NAME_TRUNCATE_LENGTH) + "…"
        } else {
            item.name
        }
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("删除「$truncatedName」？") },
            text = { Text("删除后不可恢复。") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    isRemoved = true
                }) {
                    Text("删除", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("取消")
                }
            }
        )
    }
}

/** Falls back to a generic icon when the item has no category (category is optional in v2). */
fun categoryIcon(category: FoodCategory?): ImageVector = when (category) {
    null -> Icons.Outlined.Inventory2
    FoodCategory.PRODUCE -> Icons.Outlined.LocalFlorist
    FoodCategory.MEAT -> Icons.Outlined.LunchDining
    FoodCategory.SEAFOOD -> Icons.Outlined.SetMeal
    FoodCategory.DAIRY -> Icons.Outlined.EggAlt
    FoodCategory.FROZEN -> Icons.Outlined.AcUnit
    FoodCategory.BEVERAGE -> Icons.Outlined.LocalCafe
    FoodCategory.CONDIMENT -> Icons.Outlined.Liquor
    FoodCategory.OTHER -> Icons.Outlined.MoreHoriz
}
