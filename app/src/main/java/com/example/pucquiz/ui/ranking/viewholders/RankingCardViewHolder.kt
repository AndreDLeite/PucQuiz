package com.example.pucquiz.ui.ranking.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ranking.view.*

class RankingCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var userPosition = itemView.textView_position
    var userInitials = itemView.textView_user_initials
    var userName = itemView.textView_user_name
    var userScore = itemView.textView_user_score
}