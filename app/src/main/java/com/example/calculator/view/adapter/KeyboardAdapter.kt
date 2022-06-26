package com.example.calculator.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R
import com.example.calculator.viewmodel.Key
import com.example.calculator.viewmodel.Keyboard
import com.example.calculator.viewmodel.Keys

class KeyboardAdapter(
    private val keyboard: Keyboard,
    private val keys: MutableList<Key>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val clearKeyPosition: Int = keys.indexOf(Keys.AllClear)

    private companion object {
        const val VIEW_TYPE_COMMON = 0
        const val VIEW_TYPE_WIDE = 1
        const val VIEW_TYPE_HIGHLIGHTED = 2
        const val VIEW_TYPE_CLEAR = 3
    }

    private inner class CommonKeyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.key)
    }

    private inner class WideKeyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.key)
    }

    private inner class HighlightedKeyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.key)
    }

    private inner class ClearKeyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.key)
    }

    override fun getItemViewType(position: Int): Int {
        return when (keys[position]) {
            Keys.Clear, Keys.AllClear -> VIEW_TYPE_CLEAR
            Keys.Equal -> VIEW_TYPE_WIDE
            Keys.Plus, Keys.Minus, Keys.Multiply, Keys.Divide -> VIEW_TYPE_HIGHLIGHTED
            else -> VIEW_TYPE_COMMON
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HIGHLIGHTED -> HighlightedKeyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.key_highlighted, parent, false)
            )
            VIEW_TYPE_CLEAR -> ClearKeyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.key_clear, parent, false)
            )
            VIEW_TYPE_WIDE -> WideKeyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.key_wide, parent, false)
            )
            else -> CommonKeyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.key_common, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val key = keys[position]
        when (holder.itemViewType) {
            VIEW_TYPE_CLEAR -> (holder as ClearKeyViewHolder).apply {
                button.text = key.label
                button.setOnClickListener { keyboard.pressKey(key) }
                button.contentDescription = key.label
            }
            VIEW_TYPE_WIDE -> (holder as WideKeyViewHolder).apply {
                button.text = key.label
                button.setOnClickListener { keyboard.pressKey(key) }
                button.contentDescription = key.label
            }
            VIEW_TYPE_HIGHLIGHTED -> (holder as HighlightedKeyViewHolder).apply {
                button.text = key.label
                button.setOnClickListener { keyboard.pressKey(key) }
                button.contentDescription = key.label
            }
            else -> (holder as CommonKeyViewHolder).apply {
                button.text = key.label
                button.setOnClickListener { keyboard.pressKey(key) }
                button.contentDescription = key.label
            }
        }
    }

    override fun getItemCount() = keys.size

    fun changeClearKey() {
        notifyItemChanged(clearKeyPosition)
    }
}