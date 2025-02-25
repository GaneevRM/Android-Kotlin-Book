package com.ganeevrm.android.codapizza.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pizza(
    val toppings: Map<Topping, ToppingPlacement> = emptyMap(),
    val size: Size = Size.Small
) : Parcelable {
    val price: Double
        get() = 9.99 + toppings.asSequence().sumOf { (_, toppingPlacement) ->
            when (toppingPlacement) {
                ToppingPlacement.Left, ToppingPlacement.Right -> 0.5
                ToppingPlacement.All -> 1.0
            }
        } + when (size) {
            Size.Small -> 0.5
            Size.Medium -> 1.0
            Size.Large -> 1.5
            Size.Extra -> 2.0
        }

    fun withTopping(topping: Topping, placement: ToppingPlacement?): Pizza {
        return copy(
            toppings = if (placement == null) {
                toppings - topping
            } else {
                toppings + (topping to placement)
            }
        )
    }

    fun editSize(size: Size): Pizza {
        return copy(
            size = size
        )
    }
}