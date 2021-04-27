package com.example.pucquiz.ui.shared.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.ui.ranking.viewholders.StatusViewHolder

class StatusAdapterDelegate {

    fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        layoutToInflate: Int
    ): RecyclerView.ViewHolder {
        return StatusViewHolder(
            inflater.inflate(
                layoutToInflate,
                parent,
                false
            )
        )
    }

    fun onBindViewHolder() {
        //ignore
    }

    companion object {
        const val VIEW_TYPE_STATUS = 0
    }

}