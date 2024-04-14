package com.ganeevrm.android.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = " ",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    var requiresPolice: Int = 0,
    val suspect: String = "",
    val phoneNumber: String = ""
)