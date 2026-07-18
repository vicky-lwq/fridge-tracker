package com.fridgetracker.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.util.DateFormatUtils
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

enum class ExpiryInputMode { DATE, DAYS }

/**
 * Expiry date field with two entry modes: pick an absolute date, or enter "N days from now".
 * State is hoisted to the caller (AddEditFoodScreen) since converting the active mode's value
 * into the final absolute `expiryDate` is form/ViewModel-layer logic, not a visual concern.
 * Switching modes never clears the other mode's value.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpiryInputField(
    mode: ExpiryInputMode,
    onModeChange: (ExpiryInputMode) -> Unit,
    dateValue: LocalDate?,
    onDateValueChange: (LocalDate) -> Unit,
    dateError: Boolean,
    daysValue: String,
    onDaysValueChange: (String) -> Unit,
    daysError: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "过期日期 *",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = mode == ExpiryInputMode.DATE,
                onClick = { onModeChange(ExpiryInputMode.DATE) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    inactiveBorderColor = MaterialTheme.colorScheme.outline
                )
            ) {
                Text("选日期")
            }
            SegmentedButton(
                selected = mode == ExpiryInputMode.DAYS,
                onClick = { onModeChange(ExpiryInputMode.DAYS) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    inactiveBorderColor = MaterialTheme.colorScheme.outline
                )
            ) {
                Text("选天数")
            }
        }

        Spacer(Modifier.height(8.dp))

        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                (fadeIn(tween(200)) + slideInVertically(tween(200)) { height -> height / 4 })
                    .togetherWith(fadeOut(tween(200)) + slideOutVertically(tween(200)) { height -> -height / 4 })
            },
            label = "expiryInputMode"
        ) { activeMode ->
            when (activeMode) {
                ExpiryInputMode.DATE -> DateModeField(
                    dateValue = dateValue,
                    onDateValueChange = onDateValueChange,
                    dateError = dateError
                )
                ExpiryInputMode.DAYS -> DaysModeField(
                    daysValue = daysValue,
                    onDaysValueChange = onDaysValueChange,
                    daysError = daysError
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateModeField(
    dateValue: LocalDate?,
    onDateValueChange: (LocalDate) -> Unit,
    dateError: Boolean
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = dateValue?.let { DateFormatUtils.formatExpiryDisplay(it) } ?: "",
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("请选择过期日期") },
        isError = dateError,
        supportingText = if (dateError) {
            { Text("请选择过期日期") }
        } else {
            null
        },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Filled.CalendarToday, contentDescription = "选择日期")
            }
        },
        singleLine = true
    )

    if (showDatePicker) {
        val initialMillis = (dateValue ?: LocalDate.now())
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateValueChange(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                    }
                    showDatePicker = false
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun DaysModeField(
    daysValue: String,
    onDaysValueChange: (String) -> Unit,
    daysError: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = daysValue,
            onValueChange = onDaysValueChange,
            modifier = Modifier.width(96.dp),
            placeholder = { Text("例如：3") },
            isError = daysError,
            supportingText = if (daysError) {
                { Text("请输入1-365之间的天数") }
            } else {
                null
            },
            trailingIcon = if (daysError) {
                { Icon(Icons.Filled.Error, contentDescription = null) }
            } else {
                null
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "天后到期",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
