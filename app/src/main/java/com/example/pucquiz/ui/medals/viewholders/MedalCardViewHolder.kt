package com.example.pucquiz.ui.medals.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_medals.view.*

class MedalCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var medalCardName = itemView.textView_medal_title
    var medalCardDescription = itemView.textView_medal_descritption
    var medalCardBackground = itemView.constraintLayout_grade_selected_container
}