package com.example.pucquiz.ui.medals.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Medals
import com.example.pucquiz.ui.medals.delegates.MedalCardDelegate
import com.example.pucquiz.ui.shared.delegates.StatusAdapterDelegate

class MedalsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userMedalsList: MutableList<Medals> = mutableListOf()

    private var currentStatus: MedalsInfoStatus =
        MedalsInfoStatus.DEFAULT_USER_MEDALS_LIST

    private val medalCardDelegate = MedalCardDelegate()
    private val statusAdaptedDelegate = StatusAdapterDelegate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MedalCardDelegate.VIEW_USER_MEDAL_CARD -> {
                medalCardDelegate.onCreateViewHolder(
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
            MedalCardDelegate.VIEW_USER_MEDAL_CARD -> {
                medalCardDelegate.onBindViewHolder(
                    holder,
                    userMedalsList[position]
                )
            }
            else -> {
                statusAdaptedDelegate.onBindViewHolder()
            }
        }
    }

    override fun getItemCount(): Int {
        return totalItemCount(userMedalsList, currentStatus)
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType(currentStatus)
    }

    private fun totalItemCount(list: List<Medals>, status: MedalsInfoStatus): Int {
        return if (status == MedalsInfoStatus.DEFAULT_USER_MEDALS_LIST) list.size else 1
    }

    fun updateUserMedalsList(userCurrentMedalsList: List<Medals>) {
        currentStatus = if (userCurrentMedalsList.isEmpty()) {
            MedalsInfoStatus.EMPTY_USER_MEDALS_LIST
        } else {
            MedalsInfoStatus.DEFAULT_USER_MEDALS_LIST
        }

        userMedalsList.clear()
        userMedalsList.addAll(userCurrentMedalsList)
        notifyDataSetChanged()
    }

    fun updateStatus(status: MedalsInfoStatus) {
        currentStatus = status
        notifyDataSetChanged()
    }

    private fun itemViewType(status: MedalsInfoStatus): Int {
        return when (status) {
            MedalsInfoStatus.LOADING_USER_MEDALS_LIST,
            MedalsInfoStatus.ERROR_USER_MEDALS_LIST,
            MedalsInfoStatus.EMPTY_USER_MEDALS_LIST -> {
                StatusAdapterDelegate.VIEW_TYPE_STATUS
            }
            MedalsInfoStatus.DEFAULT_USER_MEDALS_LIST -> {
                MedalCardDelegate.VIEW_USER_MEDAL_CARD
            }
        }
    }

    enum class MedalsInfoStatus {
        LOADING_USER_MEDALS_LIST {
            override fun layout(): Int = R.layout.layout_loading_status
        },
        ERROR_USER_MEDALS_LIST {
            override fun layout(): Int = R.layout.layout_error_status
        },
        EMPTY_USER_MEDALS_LIST {
            override fun layout(): Int = R.layout.layout_empty_status
        },
        DEFAULT_USER_MEDALS_LIST {
            override fun layout(): Int = R.layout.item_medals
        };

        abstract fun layout(): Int
    }


}