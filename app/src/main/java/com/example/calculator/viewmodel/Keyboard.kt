package com.example.calculator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.App
import com.example.calculator.R
import com.example.calculator.model.IProcessor

interface Keyboard {

    fun enableACKey(enabled: Boolean)
    fun pressKey(key: Key)
}

class KeyboardViewModel(private val processor: IProcessor) : ViewModel(), Keyboard {

    class Factory(private val processor: IProcessor) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return KeyboardViewModel(processor) as T
        }
    }

    private val _keys = MutableLiveData(KeyboardSetup.setup)
    val keys: LiveData<MutableList<Key>> = _keys

    override fun enableACKey(enabled: Boolean) {
        _keys.value?.set(KeyboardSetup.clearKeyPosition, if (enabled) Keys.AllClear else Keys.Clear)
        _keys.value = _keys.value // To notify Observers via setValue()
    }

    override fun pressKey(key: Key) {
        processor.processKey(key)
    }
}

private object KeyboardSetup {
    val setup = mutableListOf(
        Keys.AllClear, Keys.PlusMinus, Keys.Percent, Keys.Divide,
        Keys.Seven, Keys.Eight, Keys.Nine, Keys.Multiply,
        Keys.Four, Keys.Five, Keys.Six, Keys.Minus,
        Keys.One, Keys.Two, Keys.Three, Keys.Plus,
        Keys.Point, Keys.Zero, Keys.Equal
    )
    val clearKeyPosition: Int = setup.indexOf(Keys.AllClear)
}

object Keys { // This singleton class depends on App's context.
    val One = Key(App.res.getString(R.string.one), KeyType.Digit)
    val Two = Key(App.res.getString(R.string.two), KeyType.Digit)
    val Three = Key(App.res.getString(R.string.three), KeyType.Digit)
    val Four = Key(App.res.getString(R.string.four), KeyType.Digit)
    val Five = Key(App.res.getString(R.string.five), KeyType.Digit)
    val Six = Key(App.res.getString(R.string.six), KeyType.Digit)
    val Seven = Key(App.res.getString(R.string.seven), KeyType.Digit)
    val Eight = Key(App.res.getString(R.string.eight), KeyType.Digit)
    val Nine = Key(App.res.getString(R.string.nine), KeyType.Digit)
    val Zero = Key(App.res.getString(R.string.zero), KeyType.Digit)

    val Plus = Key(App.res.getString(R.string.plus), KeyType.Function)
    val Minus = Key(App.res.getString(R.string.minus), KeyType.Function)
    val Multiply = Key(App.res.getString(R.string.multiply), KeyType.Function)
    val Divide = Key(App.res.getString(R.string.divide), KeyType.Function)

    val PlusMinus = Key(App.res.getString(R.string.plus_minus), KeyType.Function)
    val Percent = Key(App.res.getString(R.string.percent), KeyType.Function)
    val Point = Key(App.res.getString(R.string.point), KeyType.Function)

    val Equal = Key(App.res.getString(R.string.equal), KeyType.Action)
    val Clear = Key(App.res.getString(R.string.clear), KeyType.Action)
    val AllClear = Key(App.res.getString(R.string.all_clear), KeyType.Action)

    val KeysList = listOf(
        One, Two, Three, Four, Five,
        Six, Seven, Eight, Nine, Zero,
        Plus, Minus, Multiply, Divide, PlusMinus,
        Percent, Point, Equal, Clear, AllClear
    )
}

data class Key(val label: String, val type: KeyType)

enum class KeyType {
    Digit,
    Function,
    Action
}