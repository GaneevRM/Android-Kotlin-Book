package com.ganeevrm.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ganeevrm.android.criminalintent.Crime

@Database(
    entities = [Crime::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}

val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''")
    }
}