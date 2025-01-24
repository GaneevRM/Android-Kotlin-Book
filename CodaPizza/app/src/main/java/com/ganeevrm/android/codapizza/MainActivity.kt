package com.ganeevrm.android.codapizza

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ganeevrm.android.codapizza.model.Topping
import com.ganeevrm.android.codapizza.model.ToppingPlacement
import com.ganeevrm.android.codapizza.ui.PizzaBuilderScreen
import com.ganeevrm.android.codapizza.ui.ToppingCell

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzaBuilderScreen()
        }
    }
}