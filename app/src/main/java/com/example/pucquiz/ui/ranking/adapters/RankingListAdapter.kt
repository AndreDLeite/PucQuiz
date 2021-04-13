package com.example.pucquiz.ui.ranking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.ui.ranking.delegates.RankingCardDelegate
import com.example.pucquiz.ui.ranking.delegates.RankingCardDelegate.Companion.VIEW_USER_RANKING_CARD
import com.example.pucquiz.ui.ranking.delegates.StatusAdapterDelegate

class RankingListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userInfoList: MutableList<UserAdditionalInfo> = mutableListOf()

    private var currentStatus: UserInfoStatus = UserInfoStatus.LOADING_USER_INFO_LIST

    private val rankingCardDelegate = RankingCardDelegate()
    private val statusAdaptedDelegate = StatusAdapterDelegate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_USER_RANKING_CARD -> {
                rankingCardDelegate.onCreateViewHolder(
                    inflater = LayoutInflater.from(parent.context),
                    parent = parent
                )
            }
            else -> {
                statusAdaptedDelegate.onCreateViewHolder(
                    inflater = LayoutInflater.from(parent.context),
                    parent = parent,
                    layoutToInflate = currentStatus.layout()
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_USER_RANKING_CARD -> {
                rankingCardDelegate.onBindViewHolder(
                    holder,
                    userInfoList[position],
                    position
                )
            }
            else -> {
                statusAdaptedDelegate.onBindViewHolder()
            }
        }
    }

    override fun getItemCount(): Int {
        return totalItemCount(userInfoList, currentStatus)
    }

    private fun totalItemCount(list: List<UserAdditionalInfo>, status: UserInfoStatus): Int {
        return if (status == UserInfoStatus.DEFAULT_USER_INFO_LIST) list.size else 1
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType(currentStatus)
    }

    fun updateUserInfoList(userAdditionalInfoList: List<UserAdditionalInfo>) {
        currentStatus = if (userAdditionalInfoList.isEmpty()) {
            UserInfoStatus.EMPTY_USER_INFO_LIST
        } else {
            UserInfoStatus.DEFAULT_USER_INFO_LIST
        }

        userInfoList.clear()
        userInfoList.addAll(userAdditionalInfoList)
        notifyDataSetChanged()
    }

    fun updateStatus(status: UserInfoStatus) {
        currentStatus = status
        notifyDataSetChanged()
    }

    private fun itemViewType(status: UserInfoStatus): Int {
        return when (status) {
            UserInfoStatus.LOADING_USER_INFO_LIST,
            UserInfoStatus.ERROR_USER_INFO_LIST,
            UserInfoStatus.EMPTY_USER_INFO_LIST -> {
                StatusAdapterDelegate.VIEW_TYPE_STATUS
            }
            UserInfoStatus.DEFAULT_USER_INFO_LIST -> {
                VIEW_USER_RANKING_CARD
            }
        }
    }

    enum class UserInfoStatus {
        LOADING_USER_INFO_LIST {
            override fun layout(): Int = R.layout.layout_loading_status
        },
        ERROR_USER_INFO_LIST {
            override fun layout(): Int = R.layout.layout_error_status
        },
        EMPTY_USER_INFO_LIST {
            override fun layout(): Int = R.layout.layout_empty_status
        },
        DEFAULT_USER_INFO_LIST {
            override fun layout(): Int = R.layout.item_ranking
        };

        abstract fun layout(): Int
    }

}