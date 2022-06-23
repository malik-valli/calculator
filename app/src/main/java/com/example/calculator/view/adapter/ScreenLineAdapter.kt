package com.example.calculator.view.adapter

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.App
import com.example.calculator.R

class ScreenLineAdapter(
    private var currentLine: String,
    private var historyLines: MutableList<String>
) :
    RecyclerView.Adapter<ScreenLineAdapter.ViewHolder>() {

    init {
        historyLines.reverse()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.line)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.screen_line, parent, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.textView.text = currentLine
            holder.textView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                App.res.getDimension(R.dimen.current_line_text_size)
            )
        } else {
            holder.textView.text = historyLines[position - 1]
            holder.textView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                App.res.getDimension(R.dimen.history_line_text_size)
            )
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