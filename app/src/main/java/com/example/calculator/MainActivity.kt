package com.example.calculator

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.model.Processor
import com.example.calculator.view.adapter.KeyboardKeyAdapter
import com.example.calculator.view.adapter.ScreenLineAdapter
import com.example.calculator.viewmodel.KeyboardViewModel
import com.example.calculator.viewmodel.ScreenViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var keyboardRecyclerView: RecyclerView
    private lateinit var screenRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyboardViewModel: KeyboardViewModel by viewModels()
        val screenViewModel: ScreenViewModel by viewModels()
        screenViewModel.keyboard = keyboardViewModel
        keyboardViewModel.processor = Processor
        Processor.screen = screenViewModel
        Processor.keyboard = keyboardViewModel

        val numOfKeyboardColumns = resources.getInteger(R.integer.num_of_keyboard_columns)

        keyboardRecyclerView = binding.keyboard
        val keyboardKeyAdapter = KeyboardKeyAdapter(
            keyboardViewModel,
            keyboardViewModel.keys.value ?: mutableListOf(),
        )
        keyboardRecyclerView.adapter = keyboardKeyAdapter
        keyboardViewModel.keys.observe(this) {
            keyboardKeyAdapter.changeClearKey()
        }
        keyboardRecyclerView.layoutManager = GridLayoutManager(this, numOfKeyboardColumns)
        keyboardRecyclerView.setHasFixedSize(true)

        screenRecyclerView = binding.screen
        val screenLineAdapter = ScreenLineAdapter(
            screenViewModel.currentLine.value ?: "",
            screenViewModel.historyLines.value ?: mutableListOf()
        )
        screenRecyclerView.adapter = screenLineAdapter
        screenViewModel.currentLine.observe(this) {
            screenLineAdapter.updateData(
                it ?: "",
                screenViewModel.historyLines.value ?: mutableListOf()
            )
        }
        screenViewModel.historyLines.observe(this) {
            screenLineAdapter.updateData(
                screenViewModel.currentLine.value ?: "",
                it ?: mutableListOf()
            )
        }
        screenRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
            stackFromEnd = true
        }
    }
}