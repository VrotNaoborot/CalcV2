package com.example.calcv2.history

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CalcHistory::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun calcHistoryDao(): CalcHistoryDao
}