package com.example.pucquiz.ui.medals.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.shared.Resource
import com.example.pucquiz.ui.medals.adapters.MedalsAdapter
import com.example.pucquiz.ui.medals.viewmodels.MedalsViewModel
import kotlinx.android.synthetic.main.fragment_medals.*
import org.koin.android.ext.android.inject
import java.util.*

class MedalsFragment : Fragment() {

    private val medalsViewModel by inject<MedalsViewModel>()

    private lateinit var medalsAdapter: MedalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_medals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callServices()
        initRecyclerView()
        overrideOnBackPressed()
        setupViewModelObservers()
    }

    private fun callServices() {
        medalsViewModel.fetchUsersMedals()
    }

    private fun initRecyclerView() {
        medalsAdapter = MedalsAdapter()
        recyclerView_medals.adapter = medalsAdapter
        recyclerView_medals.layoutManager = LinearLayoutManager(activity)
    }

    private fun setupViewModelObservers() {
        medalsViewModel.userMedals.observe(viewLifecycleOwner, Observer { itResource ->
            itResource ?: return@Observer

            when (itResource.status) {
                Resource.Status.SUCCESS -> {
                    itResource.data?.let {
                        medalsAdapter.updateUserMedalsList(it)
                    }
                }
                Resource.Status.ERROR -> {
                    medalsAdapter.updateStatus(MedalsAdapter.MedalsInfoStatus.ERROR_USER_MEDALS_LIST)
                }
                Resource.Status.LOADING -> {
                    medalsAdapter.updateStatus(MedalsAdapter.MedalsInfoStatus.LOADING_USER_MEDALS_LIST)
                }
            }

        })
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }
}