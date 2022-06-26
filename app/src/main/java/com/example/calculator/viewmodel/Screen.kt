package com.example.calculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface Screen {

    fun renderCurrentLine(line: String)
    fun addHistoryLine(line: String, result: String)
    fun clearCurrentLine()
    fun clearAll()
}

class ScreenViewModel(private val keyboard: Keyboard) : ViewModel(), Screen {

    class Factory(private val keyboard: Keyboard) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ScreenViewModel(keyboard) as T
        }
    }

    private val _currentLine = MutableLiveData("")
    val currentLine: LiveData<String> = _currentLine

    private val _historyLines = MutableLiveData(mutableListOf<String>())
    val historyLines: LiveData<MutableList<String>> = _historyLines

    override fun renderCurrentLine(line: String) {
        _currentLine.value = line
    }

    override fun addHistoryLine(line: String, result: String) {
        _historyLines.value?.add("$line = $result")
        _historyLines.value = _historyLines.value // To notify Observers via setValue()

        keyboard.enableACKey(false)
    }

    override fun clearCurrentLine() {
        _currentLine.value = ""

        keyboard.enableACKey(true)
    }

    override fun clearAll() {
        _currentLine.value = ""
        _historyLines.value = mutableListOf()
    }
}