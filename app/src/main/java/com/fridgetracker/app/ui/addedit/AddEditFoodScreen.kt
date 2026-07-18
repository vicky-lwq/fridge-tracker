package com.fridgetracker.app.ui.addedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.data.FoodCategory
import com.fridgetracker.app.data.FoodItem
import com.fridgetracker.app.ui.components.CategoryDropdownField
import com.fridgetracker.app.util.DateFormatUtils
import com.fridgetracker.app.viewmodel.FoodViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFoodScreen(
    viewModel: FoodViewModel,
    foodId: Long?,
    onDone: () -> Unit
) {
    val isEditMode = foodId != null

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf<FoodCategory?>(null) }
    var expiryDate by remember { mutableStateOf<LocalDate?>(null) }
    var addedDate by remember { mutableStateOf(LocalDate.now()) }

    var initialName by remember { mutableStateOf("") }
    var initialCategory by remember { mutableStateOf<FoodCategory?>(null) }
    var initialExpiryDate by remember { mutableStateOf<LocalDate?>(null) }

    var nameError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }
    var expiryError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    LaunchedEffect(foodId) {
        if (foodId != null) {
            viewModel.getById(foodId)?.let { item ->
                name = item.name
                category = item.category
                expiryDate = item.expiryDate
                addedDate = item.addedDate
                initialName = item.name
                initialCategory = item.category
                initialExpiryDate = item.expiryDate
            }
        }
    }

    fun hasUnsavedChanges(): Boolean =
        name != initialName || category != initialCategory || expiryDate != initialExpiryDate

    fun attemptCancel() {
        if (hasUnsavedChanges()) showDiscardDialog = true else onDone()
    }

    fun attemptSave() {
        nameError = name.isBlank()
        categoryError = category == null
        expiryError = expiryDate == null
        if (nameError || categoryError || expiryError) return

        val currentCategory = category ?: return
        val currentExpiry = expiryDate ?: return
        if (isEditMode && foodId != null) {
            viewModel.updateFood(
                FoodItem(
                    id = foodId,
                    name = name.trim(),
                    category = currentCategory,
                    addedDate = addedDate,
                    expiryDate = currentExpiry
                )
            )
        } else {
            viewModel.addFood(name.trim(), currentCategory, currentExpiry)
        }
        onDone()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "编辑食物" else "新增食物") },
                navigationIcon = {
                    IconButton(onClick = { attemptCancel() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (nameError) nameError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("名称 *") },
                    placeholder = { Text("例如：胡萝卜") },
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text("请输入食物名称") }
                    } else {
                        null
                    },
                    trailingIcon = if (nameError) {
                        { Icon(Icons.Filled.Error, contentDescription = null) }
                    } else {
                        null
                    },
                    singleLine = true
                )
                Text(
                    text = "录入于${DateFormatUtils.formatAddedDisplay(addedDate)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            CategoryDropdownField(
                selected = category,
                onSelect = {
                    category = it
                    categoryError = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                isError = categoryError,
                supportingText = if (categoryError) "请选择分类" else null
            )

            OutlinedTextField(
                value = expiryDate?.let { DateFormatUtils.formatExpiryDisplay(it) } ?: "",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                label = { Text("过期日期 *") },
                placeholder = { Text("请选择过期日期") },
                isError = expiryError,
                supportingText = if (expiryError) {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { attemptCancel() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("取消")
                }
                Button(
                    onClick = { attemptSave() },
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp)
                ) {
                    Text("保存")
                }
            }
        }
    }

    if (showDatePicker) {
        val initialMillis = (expiryDate ?: LocalDate.now())
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        expiryDate = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        expiryError = false
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
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("放弃更改？") },
            text = { Text("你所做的修改还没有保存。") },
            confirmButton = {
                TextButton(onClick = {
                    showDiscardDialog = false
                    onDone()
                }) {
                    Text("放弃")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text("继续编辑")
                }
            }
        )
    }
}
