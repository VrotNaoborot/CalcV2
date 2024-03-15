package com.example.calcv2.history
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calc_history")
data class CalcHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expression: String,
    val result: String
)
