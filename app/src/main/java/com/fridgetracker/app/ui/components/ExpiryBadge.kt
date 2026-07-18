package com.fridgetracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.data.ExpiryStatus
import com.fridgetracker.app.ui.theme.extendedColors

/**
 * Three-state expiry badge: normal / soon (即将过期) / expired (已过期).
 * Text + icon double channel — color alone never carries the status.
 */
@Composable
fun ExpiryBadge(remainingDays: Long, modifier: Modifier = Modifier) {
    val style = badgeStyle(remainingDays)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(style.background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (style.icon != null) {
            Icon(
                imageVector = style.icon,
                contentDescription = null,
                tint = style.content,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(text = style.text, style = MaterialTheme.typography.labelLarge, color = style.content)
    }
}

private data class BadgeStyle(
    val background: Color,
    val content: Color,
    val icon: ImageVector?,
    val text: String
)

@Composable
private fun badgeStyle(remainingDays: Long): BadgeStyle = when (ExpiryStatus.from(remainingDays)) {
    ExpiryStatus.NORMAL -> BadgeStyle(
        background = MaterialTheme.colorScheme.surfaceContainerHigh,
        content = MaterialTheme.colorScheme.onSurfaceVariant,
        icon = null,
        text = "剩${remainingDays}天"
    )
    ExpiryStatus.SOON -> BadgeStyle(
        background = MaterialTheme.extendedColors.warningContainer,
        content = MaterialTheme.extendedColors.onWarningContainer,
        icon = Icons.Outlined.Schedule,
        text = if (remainingDays == 0L) "今天到期" else "剩${remainingDays}天"
    )
    ExpiryStatus.EXPIRED -> BadgeStyle(
        background = MaterialTheme.colorScheme.errorContainer,
        content = MaterialTheme.colorScheme.onErrorContainer,
        icon = Icons.Outlined.WarningAmber,
        text = "已过期${-remainingDays}天"
    )
}
