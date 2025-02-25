package com.ganeevrm.android.codapizza.model

import androidx.annotation.StringRes
import com.ganeevrm.android.codapizza.R

enum class Size(@StringRes val size: Int) {
    Small(R.string.size_small),
    Medium(R.string.size_medium),
    Large(R.string.size_large),
    Extra(R.string.size_extra_large)
}