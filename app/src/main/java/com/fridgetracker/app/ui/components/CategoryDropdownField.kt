package com.fridgetracker.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
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
import androidx.compose.ui.text.style.TextOverflow
import com.fridgetracker.app.data.FoodCategory

/**
 * Single-select category picker for the add/edit form. Deliberately a different
 * component shape than CategoryFilterChipRow: this is single-value form entry,
 * that is multi-select browsing — different scenarios, not an inconsistency.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownField(
    selected: FoodCategory?,
    onSelect: (FoodCategory) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String? = null
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
            value = selected?.displayName ?: "",
            onValueChange = {},
            label = { Text("分类 *") },
            placeholder = { Text("请选择分类") },
            isError = isError,
            supportingText = supportingText?.let { { Text(it) } },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true,
            maxLines = 1
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            FoodCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            category.displayName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        onSelect(category)
                        expanded = false
                    },
                    colors = if (category == selected) {
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
