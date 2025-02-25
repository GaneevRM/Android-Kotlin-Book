package com.ganeevrm.android.codapizza.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ganeevrm.android.codapizza.R
import com.ganeevrm.android.codapizza.model.Pizza
import com.ganeevrm.android.codapizza.model.Topping
import java.text.NumberFormat

@Preview
@Composable
fun PizzaBuilderScreen(modifier: Modifier = Modifier) {
    var pizza by rememberSaveable {
        mutableStateOf(Pizza())
    }
    var rotatePizza by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            SizeDropdownMenu(
                pizza = pizza,
                onEditPizza = { pizza = it },
                modifierButton = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            ToppingList(
                pizza = pizza,
                onEditPizza = { pizza = it },
                rotateTrigger = rotatePizza,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            )

            OrderButton(
                pizza = pizza,
                onOrderClick = { rotatePizza = !rotatePizza },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun ToppingList(
    pizza: Pizza,
    onEditPizza: (Pizza) -> Unit,
    rotateTrigger: Boolean,
    modifier: Modifier = Modifier
) {

    var toppingBeingAdded by rememberSaveable { mutableStateOf<Topping?>(null) }

    toppingBeingAdded?.let { topping ->
        ToppingPlacementDialog(
            topping = topping,
            onSetToppingPlacement = { placement ->
                onEditPizza(pizza.withTopping(topping, placement))
                topping.toppingName
            },
            onDismissRequest = {
                toppingBeingAdded = null
            }
        )
    }

    LazyColumn(modifier = modifier) {
        item {
            PizzaHeroImage(
                pizza = pizza,
                rotateTrigger = rotateTrigger,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        items(Topping.entries.toTypedArray()) { topping ->
            ToppingCell(
                topping = topping,
                placement = pizza.toppings[topping],
                onClickTopping = {
                    toppingBeingAdded = topping
                })
        }
    }
}

@Composable
private fun OrderButton(pizza: Pizza, onOrderClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(modifier = modifier, onClick = {
        onOrderClick()
        Toast.makeText(context, R.string.order_placed_toast, Toast.LENGTH_LONG)
            .show()
    }) {
        val currencyFormatter = remember {
            NumberFormat.getCurrencyInstance()
        }
        val price = currencyFormatter.format(pizza.price)
        Text(text = stringResource(R.string.place_order_button, price).toUpperCase(Locale.current))
    }
}
