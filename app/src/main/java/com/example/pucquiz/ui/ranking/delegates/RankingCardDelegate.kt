package com.example.pucquiz.ui.ranking.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.ui.ranking.viewholders.RankingCardViewHolder

class RankingCardDelegate {

    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return RankingCardViewHolder(
            inflater.inflate(
                R.layout.item_ranking,
                parent,
                false
            )
        )
    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        userInfo: UserAdditionalInfo,
        position: Int
    ) {
        with(holder as RankingCardViewHolder) {
            val realPosition = position+1
            val userScoreString = holder.itemView.context.getString(R.string.ranking_user_points)
            userPosition.text = "$realPosition."
            userName.text = userInfo.userName
            userInitials.text = userInfo.getUserInitials()
            userScore.text = String.format(userScoreString, userInfo.userScore)
        }
    }

    companion object {
        const val VIEW_USER_RANKING_CARD = 621
    }

}