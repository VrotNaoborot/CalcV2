package com.example.calcv2.history

import androidx.room.Dao

import androidx.room.Insert

import androidx.room.Query


@Dao
interface CalcHistoryDao {
    @Insert
    fun insert(history: CalcHistory)

    @Query("SELECT * FROM calc_history")
    fun getAllHistory(): List<CalcHistory>

    @Query("DELETE FROM calc_history")
    fun deleteAll()
}