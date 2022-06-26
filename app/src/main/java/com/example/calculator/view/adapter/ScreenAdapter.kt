package com.example.calculator.view.adapter

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R

class ScreenAdapter(
    private var currentLine: String,
    private var historyLines: MutableList<String>,
    private val currentLineTextSize: Float,
    private val historyLineTextSize: Float
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        historyLines.reverse()
    }

    private companion object {
        const val VIEW_TYPE_CURRENT = 0
        const val VIEW_TYPE_HISTORY = 1
    }

    private inner class CurrentLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.line)
    }

    private inner class HistoryLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.line)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_CURRENT else VIEW_TYPE_HISTORY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CURRENT) CurrentLineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.current_line, parent, false)
        ) else HistoryLineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_CURRENT) {
            (holder as CurrentLineViewHolder).apply {
                textView.text = currentLine
                holder.textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    currentLineTextSize
                )
            }
        } else {
            (holder as HistoryLineViewHolder).apply {
                textView.text = historyLines[position - 1]
                holder.textView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    historyLineTextSize
                )
            }
        }
    }

    override fun getItemCount() = historyLines.size + 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(currentLine: String, historyLines: MutableList<String>) {
        this.currentLine = currentLine
        this.historyLines = historyLines.asReversed()
        notifyDataSetChanged()
    }
}