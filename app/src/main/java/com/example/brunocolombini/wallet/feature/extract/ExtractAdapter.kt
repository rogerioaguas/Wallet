package com.example.brunocolombini.wallet.feature.extract

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.R

class ExtractAdapter
constructor(
        private val context: Context,
        private val extracts: List<Extract>
) : RecyclerView.Adapter<ExtractAdapter.ExtractHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExtractHolder {
        return ExtractHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.adapter_extract, parent, false))
    }

    override fun getItemCount(): Int = extracts.size

    override fun onBindViewHolder(holder: ExtractHolder?, position: Int) {
        holder?.let { entity ->
            val extractPosition = extracts.get(position)
            entity.amount.setTextColor(ContextCompat.getColor(context, when (extractPosition.amount >= 0) {
                true -> android.R.color.holo_green_dark
                false -> android.R.color.holo_red_dark
            }))
            entity.coin.text = extractPosition.coin
            entity.amount.text = extractPosition.amount.toString()
            entity.date.text = extractPosition.date
        }
    }


    inner class ExtractHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var coin: TextView = itemView.findViewById(R.id.coin)
        var amount: TextView = itemView.findViewById(R.id.amount)
        var date: TextView = itemView.findViewById(R.id.date)
    }
}