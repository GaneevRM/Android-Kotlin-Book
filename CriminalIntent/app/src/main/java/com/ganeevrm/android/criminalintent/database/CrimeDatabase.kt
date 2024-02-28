package com.ganeevrm.android.criminalintent.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ganeevrm.android.criminalintent.Crime

@Database(
    entities = [Crime::class],
    version = 2,
    autoMigrations = [AutoMigration(1, 2)],
    exportSchema = true
)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}