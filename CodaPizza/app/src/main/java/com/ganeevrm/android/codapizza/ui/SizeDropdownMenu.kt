package com.ganeevrm.android.codapizza.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ganeevrm.android.codapizza.model.Pizza
import com.ganeevrm.android.codapizza.model.Size

@Composable
fun SizeDropdownMenu(
    pizza: Pizza,
    onEditPizza: (Pizza) -> Unit,
    modifierButton: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val sizes = Size.entries
    var selectedOption by remember { mutableStateOf(sizes[0]) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Button(modifier = modifierButton, onClick = { expanded = true }) {
            Text(stringResource(selectedOption.size))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sizes.forEach { size ->
                DropdownMenuItem(
                    onClick = {
                        onEditPizza(pizza.editSize(size))
                        selectedOption = size
                        expanded = false
                    }
                ) { Text(stringResource(size.size)) }
            }
        }
    }
}