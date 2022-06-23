package com.example.calculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface Screen {

    fun renderCurrentLine(line: String)
    fun addHistoryLine(line: String, result: String)
    fun clearCurrentLine()
    fun clearAll()
}

class ScreenViewModel : ViewModel(), Screen {

    lateinit var keyboard: Keyboard

    private val _currentLine = MutableLiveData("")
    val currentLine: LiveData<String> = _currentLine

    private val _historyLines =
        MutableLiveData(mutableListOf<String>()) //TODO create a model for history lines
    val historyLines: LiveData<MutableList<String>> = _historyLines

    override fun renderCurrentLine(line: String) {
        _currentLine.value = line
    }

    override fun addHistoryLine(line: String, result: String) {
        _historyLines.value?.add("$line = $result")
        _historyLines.value = _historyLines.value // To notify Observers via setValue()

        keyboard.enableAC(false)
    }

    override fun clearCurrentLine() {
        _currentLine.value = ""

        keyboard.enableAC(true)
    }

    override fun clearAll() {
        _currentLine.value = ""
        _historyLines.value = mutableListOf()
    }
}