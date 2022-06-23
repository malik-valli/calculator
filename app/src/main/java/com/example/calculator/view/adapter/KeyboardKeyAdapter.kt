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

class KeyboardKeyAdapter(
    private val keyboard: Keyboard,
    private val keys: MutableList<Key>
) : RecyclerView.Adapter<KeyboardKeyAdapter.ViewHolder>() {

    private val clearKeyPosition: Int = keys.indexOf(Keys.AllClear)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.key)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.keyboard_key, parent, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = keys[position]
        holder.button.text = key.label
        holder.button.setOnClickListener { keyboard.pressKey(key) }
        holder.button.contentDescription = key.label
    }

    override fun getItemCount() = keys.size

    fun changeClearKey() {
        notifyItemChanged(clearKeyPosition)
    }
}