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
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
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

/**
 * Swipe-to-delete list card. Swiping past the threshold plays a 200ms shrink+fade
 * exit before `onDelete` actually fires, so the row is still on screen while animating.
 * Not going past the threshold lets SwipeToDismissBoxState's default spring settle back.
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

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.StartToEnd || value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
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
                        Text(
                            text = item.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    ExpiryBadge(remainingDays = item.remainingDays())
                }
            }
        }
    }
}

fun categoryIcon(category: FoodCategory): ImageVector = when (category) {
    FoodCategory.PRODUCE -> Icons.Outlined.LocalFlorist
    FoodCategory.MEAT -> Icons.Outlined.LunchDining
    FoodCategory.SEAFOOD -> Icons.Outlined.SetMeal
    FoodCategory.DAIRY -> Icons.Outlined.EggAlt
    FoodCategory.FROZEN -> Icons.Outlined.AcUnit
    FoodCategory.BEVERAGE -> Icons.Outlined.LocalCafe
    FoodCategory.CONDIMENT -> Icons.Outlined.Liquor
    FoodCategory.OTHER -> Icons.Outlined.MoreHoriz
}
