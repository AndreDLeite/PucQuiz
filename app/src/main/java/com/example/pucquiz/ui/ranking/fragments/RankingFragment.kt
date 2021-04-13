package com.example.pucquiz.ui.ranking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.ui.ranking.adapters.RankingListAdapter
import com.example.pucquiz.ui.ranking.viewmodels.RankingViewModel
import kotlinx.android.synthetic.main.fragment_ranking.*
import org.koin.android.ext.android.inject

class RankingFragment: Fragment() {

    private val rankingViewModel by inject<RankingViewModel>()

    private lateinit var rankingListAdapter: RankingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callServices()
        initRecyclerView()
        overrideOnBackPressed()
    }

    private fun callServices() {
        rankingViewModel.fetchUsersRankings()
    }

    private fun initRecyclerView() {
        rankingListAdapter = RankingListAdapter()
        recyclerView_ranking.adapter = rankingListAdapter
        recyclerView_ranking.layoutManager = LinearLayoutManager(activity)
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Add onBackPressed action here
        }
    }
}