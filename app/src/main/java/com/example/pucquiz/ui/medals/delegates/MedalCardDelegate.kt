package com.example.pucquiz.ui.medals.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Medals
import com.example.pucquiz.ui.medals.viewholders.MedalCardViewHolder

class MedalCardDelegate {
    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return MedalCardViewHolder(
            inflater.inflate(
                R.layout.item_medals,
                parent,
                false
            )
        )
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        userMedal: Medals
    ) {
        with(holder as MedalCardViewHolder) {
            medalCardName.text = userMedal.name
            medalCardDescription.text = userMedal.description
            if (userMedal.medalIsActive) {
                val resourceColor = userMedal.type.resourceColor()
                val context = medalCardBackground.context
                medalCardBackground.backgroundTintList =
                    ContextCompat.getColorStateList(context, resourceColor)
            }
        }
    }

    companion object {
        const val VIEW_USER_MEDAL_CARD = 34
    }
}