package com.fridgetracker.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.data.QuantityUnit

/**
 * Optional weight/count field. Neither sub-control ever enters an error state — both are
 * selective, so an empty quantity doesn't invalidate the unit and vice versa. New entries
 * default the unit to "斤" (handled by the caller's initial state, not here); editing an
 * existing item always reflects that item's stored unit.
 */
@Composable
fun QuantityInputRow(
    quantityText: String,
    onQuantityTextChange: (String) -> Unit,
    unit: QuantityUnit,
    onUnitChange: (QuantityUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = quantityText,
            onValueChange = onQuantityTextChange,
            modifier = Modifier.weight(2f),
            label = { Text("重量/数量") },
            placeholder = { Text("例如：1.5") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        QuantityUnitDropdown(
            selected = unit,
            onSelect = onUnitChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuantityUnitDropdown(
    selected: QuantityUnit,
    onSelect: (QuantityUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            value = selected.displayName,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            QuantityUnit.entries.forEach { candidate ->
                DropdownMenuItem(
                    text = { Text(candidate.displayName) },
                    onClick = {
                        onSelect(candidate)
                        expanded = false
                    },
                    colors = if (candidate == selected) {
                        MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    } else {
                        MenuDefaults.itemColors()
                    }
                )
            }
        }
    }
}
