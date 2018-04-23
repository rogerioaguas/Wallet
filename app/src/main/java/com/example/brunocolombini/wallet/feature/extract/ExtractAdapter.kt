package com.example.brunocolombini.wallet.feature.extract

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class ExtractAdapter
constructor(
        private val context: Context
) : RecyclerView.Adapter<ExtractAdapter.ExtractHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExtractHolder {

    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ExtractHolder?, position: Int) {
    }


    inner class ExtractHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}