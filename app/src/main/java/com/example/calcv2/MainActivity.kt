package com.example.calcv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.room.Room
import com.example.calcv2.databinding.ActivityMainBinding
import com.example.calcv2.history.AppDatabase
import com.example.calcv2.history.CalcHistory
import com.example.calcv2.history.HistoryActivity
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.concurrent.thread



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Создание экземпляра класса привязки и назначение переменной binding
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Инициализация базы данных из объекта MyApplication
        val myApplication = application as MyApplication
        db = myApplication.database

        // удаление данных из бд
//        thread {
//            db.calcHistoryDao().deleteAll()
//            runOnUiThread {
//                binding.historyView.text = ""
//            }
//        }
        loadAndDisplayHistory()


        // Установка содержимого активности с использованием корневого представления из класса привязки
        setContentView(binding.root)

        // Назначение слушателя нажатия на кнопку
        with(binding) {
            bSqrt.setOnClickListener {
                operation.append("sqrt(")
            }
            bLog2.setOnClickListener {
                operation.append("log2(")
            }
            bLeftb.setOnClickListener {
                operation.append("(")
            }
            bRightb.setOnClickListener {
                operation.append(")")
            }
            bDegree.setOnClickListener {
                operation.append("^")
            }
            bClear.setOnClickListener {
                operation.text = ""
                result.text = ""
            }
            bBack.setOnClickListener {
                val s = operation.text.toString()
                if (s != "") {
                    operation.text = s.substring(0,s.length-1)
                }
            }
            bProcent.setOnClickListener {
                operation.append("%")
            }
            bDivide.setOnClickListener {
                operation.append("/")
            }
            bSin.setOnClickListener {
                operation.append("sin(")
            }
            b7.setOnClickListener {
                operation.append("7")
            }
            b8.setOnClickListener {
                operation.append("8")
            }
            b9.setOnClickListener {
                operation.append("9")
            }
            bMultiply.setOnClickListener {
                operation.append("*")
            }
            bCos.setOnClickListener {
                operation.append("cos(")
            }
            b4.setOnClickListener {
                operation.append("4")
            }
            b5.setOnClickListener {
                operation.append("5")
            }
            b6.setOnClickListener {
                operation.append("6")
            }
            bMinus.setOnClickListener {
                operation.append("-")
            }
            bPi.setOnClickListener {
                operation.append("pi")
            }
            b1.setOnClickListener {
                operation.append("1")
            }
            b2.setOnClickListener {
                operation.append("2")
            }
            b3.setOnClickListener {
                operation.append("3")
            }
            bPlus.setOnClickListener {
                operation.append("+")
            }
            bE.setOnClickListener {
                operation.append("e")
            }
            bTripleZero.setOnClickListener {
                operation.append("000")
            }
            bDouble.setOnClickListener {
                operation.append(".")
            }
            bZero.setOnClickListener {
                operation.append("0")
            }
            result.setOnClickListener {
                val restext = result.text.toString()
                if (restext != "Error" && restext != ""){
                    operation.text = restext
                    result.text = ""
                }
            }
            bEq.setOnClickListener{
                val optext = operation.text.toString() //Выражение в формате строки
                if (optext != "") {
                    try {
                        val expr = ExpressionBuilder(operation.text.toString()).build() //строим выражение
                        val res = expr.evaluate() //Находим ответ (число, может быть нецелое)
                        var resultFinish = ""
                        val longres = res.toLong() //longres - число в формате long (целочисленное)
                        if (longres.toDouble() == res) { //Если число целое,
                            resultFinish = "= " + longres.toString() //То: Отбрасываем ноль после запятой

                        } else {
                            resultFinish = "= " + res.toString() //Иначе: Сохраняем числа после запятой
                        }
                        result.text = resultFinish
                        Log.d("MyLog", "Expression - ${operation.text} result - $resultFinish")
                        thread {
                            db.calcHistoryDao().insert(CalcHistory(expression = operation.text.toString(), result = resultFinish))
                        }

                    } catch (e: Exception) { //Если выражение записано некорректно
                        result.text = "Error" //В поле ответа пишем 'Error'
                    }
                }
                loadAndDisplayHistory()
            }

            historyImView.setOnClickListener {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        loadAndDisplayHistory()
    }

    fun loadAndDisplayHistory() {
        thread {
            var history = db.calcHistoryDao().getAllHistory()
            history = history.takeLast(3)
            val historyText = history.joinToString("\n") { "${it.expression} ${it.result}" }
            runOnUiThread {
                // Отобразить историю операций в текстовом поле
                binding.historyView.text = historyText
            }
        }
    }
}