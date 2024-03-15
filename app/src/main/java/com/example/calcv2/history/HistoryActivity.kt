package com.example.calcv2.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calcv2.MyApplication
import com.example.calcv2.R
import com.example.calcv2.databinding.ActivityHistoryBinding
import kotlin.concurrent.thread

class HistoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityHistoryBinding
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создание экземпляра класса привязки и назначение переменной binding
        binding = ActivityHistoryBinding.inflate(layoutInflater)

        // Инициализация базы данных из объекта MyApplication
        val myApplication = application as MyApplication
        db = myApplication.database

        setContentView(binding.root)

        loadFullDisplayHistory()

        binding.iwBack.setOnClickListener {
            finish()
        }

        binding.trash.setOnClickListener {
            thread {
                db.calcHistoryDao().deleteAll()
                runOnUiThread {
                    binding.fullHistoryView.text = ""
                }
            }
        }
    }

    fun loadFullDisplayHistory() {
        thread {
            var history = db.calcHistoryDao().getAllHistory()
            val historyText = history.joinToString("\n") { "${it.expression} ${it.result}" }
            runOnUiThread {
                // Отобразить историю операций в текстовом поле
                binding.fullHistoryView.text = historyText
            }
        }
    }
}