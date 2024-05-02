package com.example.calcv2

import android.app.Application
import androidx.room.Room
import com.example.calcv2.history.AppDatabase

class MyApplication: Application() {
    lateinit var database: AppDatabase
         private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "calc_history").build()
    }
}