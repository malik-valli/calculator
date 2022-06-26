package com.example.calculator.model

import com.example.calculator.viewmodel.Key
import com.example.calculator.viewmodel.Keyboard
import com.example.calculator.viewmodel.Keys
import com.example.calculator.viewmodel.Screen
import com.fathzer.soft.javaluator.DoubleEvaluator

interface IProcessor {

    fun processKey(key: Key)
    fun calculate(expression: String): Double
}

object Processor : IProcessor {

    lateinit var screen: Screen
    lateinit var keyboard: Keyboard

    private val line: String
        get() {
            var result = ""
            for (k in keysList) {
                result += k.label
            }
            return result
        }
    private var keysList = mutableListOf<Key>()

    override fun processKey(key: Key) {
        when (key) {
            Keys.Equal -> {
                try {
                    val result = trimTrailingZero(calculate(line).toString())
                    check(line != result)
                    screen.addHistoryLine(line, result)
                    keysList = stringToKeysList(result)
                    screen.renderCurrentLine(result)
                } catch (_: Throwable) {
                    // User has wrote invalid expression.
                }
                return
            }
            Keys.AllClear -> {
                screen.clearAll()
                keysList.clear()
            }
            Keys.Clear -> {
                screen.clearCurrentLine()
                keysList.clear()
            }
            else -> keysList.add(key)
        }

        screen.renderCurrentLine(line)
    }

    override fun calculate(expression: String): Double {
        prepareForJavaluator(expression).let {
            return DoubleEvaluator().evaluate(it)
        }
    }

    private fun prepareForJavaluator(expression: String): String {
        return expression.replace(
            Keys.Multiply.label.first(),
            DoubleEvaluator.MULTIPLY.symbol.first()
        ).replace(Keys.Divide.label.first(), DoubleEvaluator.DIVIDE.symbol.first())
    }

    private fun stringToKeysList(expression: String): MutableList<Key> {
        val result = mutableListOf<Key>()
        for (c in expression) {
            Keys.KeysList.find { it.label.first() == c }.let {
                if (it != null)
                    result.add(it)
            }
        }
        return result
    }

    private fun trimTrailingZero(value: String): String {
        return if (value.indexOf(".") < 0) value
        else value.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
    }
}