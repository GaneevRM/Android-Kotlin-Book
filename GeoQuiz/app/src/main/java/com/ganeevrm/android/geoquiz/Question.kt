package com.ganeevrm.android.geoquiz

import androidx.annotation.StringRes

data class Question (@StringRes val textResId: Int, val answer: Boolean, var userAnswer: Boolean?, var cheatAnswer: Boolean)