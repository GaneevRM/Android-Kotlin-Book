package com.ganeevrm.android.codapizza.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ganeevrm.android.codapizza.R
import com.ganeevrm.android.codapizza.model.Pizza
import com.ganeevrm.android.codapizza.model.Topping
import com.ganeevrm.android.codapizza.model.ToppingPlacement.All
import com.ganeevrm.android.codapizza.model.ToppingPlacement.Left
import com.ganeevrm.android.codapizza.model.ToppingPlacement.Right

@Preview
@Composable
private fun PizzaHeroImagePreview() {
    PizzaHeroImage(
        pizza = Pizza(
            toppings = mapOf(
                Topping.Pineapple to All,
                Topping.Pepperoni to Left,
                Topping.Basil to Right
            )
        )
    )
}

@Composable
fun PizzaHeroImage(
    pizza: Pizza,
    rotateTrigger: Boolean = false,
    modifier: Modifier = Modifier
) {

    val rotation by animateFloatAsState(
        targetValue = if (rotateTrigger) 360f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "pizzaRotation"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer(rotationZ = rotation)
    ) {
        Image(
            painter = painterResource(R.drawable.pizza_crust),
            contentDescription = stringResource(id = R.string.pizza_preview),
            modifier = Modifier.fillMaxSize()
        )

        pizza.toppings.forEach { (topping, placement) ->

            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(pizza.toppings) {
                isVisible = true
            }

            val alpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(durationMillis = 1000),
                label = "pizzaAnima"
            )

            Image(
                painter = painterResource(topping.pizzaOverlayImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = when (placement) {
                    Left -> Alignment.TopStart
                    Right -> Alignment.TopEnd
                    All -> Alignment.Center
                },
                modifier = Modifier
                    .focusable(false)
                    .aspectRatio(
                        when (placement) {
                            Left, Right -> 0.5f
                            All -> 1.0f
                        }
                    )
                    .align(
                        when (placement) {
                            Left -> Alignment.CenterStart
                            Right -> Alignment.CenterEnd
                            All -> Alignment.Center
                        }
                    )
                    .alpha(alpha)
            )
        }
    }
}