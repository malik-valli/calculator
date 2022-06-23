package com.example.calculator.model

import com.example.calculator.viewmodel.*
import com.fathzer.soft.javaluator.DoubleEvaluator

interface IProcessor {

    fun processKey(key: Key)
    fun calculate(expression: String): Double
}

object Processor : IProcessor {

    lateinit var screen: Screen
    lateinit var keyboard: Keyboard

    private var line = ""
    private var keysList = mutableListOf<Key>()

    override fun processKey(key: Key) {
        if (keysList.isEmpty()) {
            if (key.type == KeyType.Digit) keysList.add(key)
            if (key == Keys.AllClear) {
                screen.clearAll()
                keysList.clear()
            }
        } else when (key.type) {
            KeyType.Digit -> if (keysList.last() == Keys.Percent) {
                keysList.addAll(listOf(Keys.Multiply, key))
            } else keysList.add(key)
            KeyType.Function ->
                if (keysList.last().type == KeyType.Function && keysList.last() != Keys.Percent)
                    keysList[keysList.lastIndex] = key
                else keysList.add(key)
            KeyType.Action -> if (key == Keys.Equal) {
                val result = trimTrailingZero(calculate(line).toString())
                screen.addHistoryLine(line, result)
                keysList = stringToKeyList(result)
                screen.renderCurrentLine(result)
                return
            } else if (key == Keys.AllClear) {
                screen.clearAll()
                keysList.clear()
            } else if (key == Keys.Clear) {
                screen.clearCurrentLine()
                keysList.clear()
            }
        }

        line = ""
        for (k in keysList) {
            line += k.label
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

    private fun stringToKeyList(expression: String): MutableList<Key> {
        val keyList = mutableListOf<Key>()
        for (c in expression) {
            Keys.KeysList.find { it.label.first() == c }.let {
                if (it != null)
                    keyList.add(it)
            }
        }
        return keyList
    }

    private fun trimTrailingZero(value: String): String {
        return if (value.indexOf(".") < 0) value
        else value.replace("0*$".toRegex(), "").replace("\\.$".toRegex(), "")
    }
}