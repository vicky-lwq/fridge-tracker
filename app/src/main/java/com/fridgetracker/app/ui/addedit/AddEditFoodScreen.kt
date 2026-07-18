package com.fridgetracker.app.ui.addedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.fridgetracker.app.data.QuantityUnit
import com.fridgetracker.app.ui.components.CategoryDropdownField
import com.fridgetracker.app.ui.components.ExpiryInputField
import com.fridgetracker.app.ui.components.ExpiryInputMode
import com.fridgetracker.app.ui.components.QuantityInputRow
import com.fridgetracker.app.util.DateFormatUtils
import com.fridgetracker.app.viewmodel.FoodViewModel
import java.time.LocalDate

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
    var addedDate by remember { mutableStateOf(LocalDate.now()) }

    var expiryMode by remember { mutableStateOf(ExpiryInputMode.DATE) }
    var expiryDateValue by remember { mutableStateOf<LocalDate?>(null) }
    var expiryDaysText by remember { mutableStateOf("") }

    var quantityText by remember { mutableStateOf("") }
    var quantityUnit by remember { mutableStateOf(QuantityUnit.JIN) }

    var initialName by remember { mutableStateOf("") }
    var initialCategory by remember { mutableStateOf<FoodCategory?>(null) }
    var initialExpiryDate by remember { mutableStateOf<LocalDate?>(null) }
    var initialQuantityText by remember { mutableStateOf("") }
    var initialQuantityUnit by remember { mutableStateOf(QuantityUnit.JIN) }

    var nameError by remember { mutableStateOf(false) }
    var expiryError by remember { mutableStateOf(false) }
    var daysError by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    LaunchedEffect(foodId) {
        if (foodId != null) {
            viewModel.getById(foodId)?.let { item ->
                name = item.name
                category = item.category
                expiryDateValue = item.expiryDate
                addedDate = item.addedDate
                quantityText = item.quantity?.let(::formatQuantity) ?: ""
                quantityUnit = item.quantityUnit ?: QuantityUnit.JIN

                initialName = item.name
                initialCategory = item.category
                initialExpiryDate = item.expiryDate
                initialQuantityText = quantityText
                initialQuantityUnit = quantityUnit
            }
        }
    }

    fun resolvedExpiryDate(): LocalDate? = when (expiryMode) {
        ExpiryInputMode.DATE -> expiryDateValue
        ExpiryInputMode.DAYS -> expiryDaysText.trim().toIntOrNull()?.let { LocalDate.now().plusDays(it.toLong()) }
    }

    fun hasUnsavedChanges(): Boolean =
        name != initialName ||
            category != initialCategory ||
            resolvedExpiryDate() != initialExpiryDate ||
            quantityText != initialQuantityText ||
            quantityUnit != initialQuantityUnit

    fun attemptCancel() {
        if (hasUnsavedChanges()) showDiscardDialog = true else onDone()
    }

    fun attemptSave() {
        nameError = name.isBlank()

        val daysInt = expiryDaysText.trim().toIntOrNull()
        when (expiryMode) {
            ExpiryInputMode.DATE -> {
                expiryError = expiryDateValue == null
                daysError = false
            }
            ExpiryInputMode.DAYS -> {
                daysError = daysInt == null || daysInt < 0 || daysInt > 365
                expiryError = false
            }
        }

        if (nameError || expiryError || daysError) return

        val currentExpiry = when (expiryMode) {
            ExpiryInputMode.DATE -> expiryDateValue ?: return
            ExpiryInputMode.DAYS -> LocalDate.now().plusDays((daysInt ?: return).toLong())
        }
        val currentQuantity = quantityText.trim().toDoubleOrNull()

        if (isEditMode && foodId != null) {
            viewModel.updateFood(
                FoodItem(
                    id = foodId,
                    name = name.trim(),
                    category = category,
                    addedDate = addedDate,
                    expiryDate = currentExpiry,
                    quantity = currentQuantity,
                    quantityUnit = quantityUnit
                )
            )
        } else {
            viewModel.addFood(
                name = name.trim(),
                category = category,
                expiryDate = currentExpiry,
                quantity = currentQuantity,
                quantityUnit = quantityUnit
            )
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
                onSelect = { category = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )

            ExpiryInputField(
                mode = expiryMode,
                onModeChange = { expiryMode = it },
                dateValue = expiryDateValue,
                onDateValueChange = {
                    expiryDateValue = it
                    expiryError = false
                },
                dateError = expiryError,
                daysValue = expiryDaysText,
                onDaysValueChange = {
                    expiryDaysText = it
                    daysError = false
                },
                daysError = daysError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )

            QuantityInputRow(
                quantityText = quantityText,
                onQuantityTextChange = { quantityText = it },
                unit = quantityUnit,
                onUnitChange = { quantityUnit = it },
                modifier = Modifier.padding(top = 24.dp)
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

private fun formatQuantity(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
