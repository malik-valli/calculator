package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.model.Processor
import com.example.calculator.view.adapter.KeyboardAdapter
import com.example.calculator.view.adapter.ScreenAdapter
import com.example.calculator.viewmodel.KeyboardViewModel
import com.example.calculator.viewmodel.ScreenViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var keyboardRecyclerView: RecyclerView
    private lateinit var screenRecyclerView: RecyclerView

    private lateinit var keyboardViewModel: KeyboardViewModel
    private lateinit var screenViewModel: ScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyboardVMFactory = KeyboardViewModel.Factory(Processor)
        keyboardViewModel =
            ViewModelProvider(this, keyboardVMFactory)[KeyboardViewModel::class.java]

        val screenVMFactory = ScreenViewModel.Factory(keyboardViewModel)
        screenViewModel = ViewModelProvider(this, screenVMFactory)[ScreenViewModel::class.java]

        Processor.screen = screenViewModel
        Processor.keyboard = keyboardViewModel

        keyboardRecyclerView = binding.keyboard
        val keyboardAdapter = KeyboardAdapter(
            keyboardViewModel,
            keyboardViewModel.keys.value ?: mutableListOf(),
        )
        keyboardRecyclerView.adapter = keyboardAdapter
        keyboardViewModel.keys.observe(this) {
            keyboardAdapter.changeClearKey()
        }
        keyboardRecyclerView.setHasFixedSize(true)

        screenRecyclerView = binding.screen
        val screenAdapter = ScreenAdapter(
            screenViewModel.currentLine.value ?: "",
            screenViewModel.historyLines.value ?: mutableListOf(),
            resources.getDimension(R.dimen.current_line_text_size),
            resources.getDimension(R.dimen.history_line_text_size)
        )
        screenRecyclerView.adapter = screenAdapter
        screenViewModel.currentLine.observe(this) {
            screenAdapter.updateData(
                it ?: "",
                screenViewModel.historyLines.value ?: mutableListOf()
            )
        }
        screenViewModel.historyLines.observe(this) {
            screenAdapter.updateData(
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