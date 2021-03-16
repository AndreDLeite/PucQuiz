package com.example.pucquiz.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.ui.register.adapters.GradeListAdapter
import com.example.pucquiz.ui.register.viewmodels.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_grades_selection.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GradeSelectionFragment : Fragment() {

    private val registerViewModel by sharedViewModel<RegisterViewModel>()

    private val gradesListAdapter = GradeListAdapter(emptyList<String>(), mutableListOf<String>())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grades_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSelectedTagsList()
        setupListeners()
        fetchValidGrades()
        initRecyclerView()
    }

    private fun loadSelectedTagsList() {
        registerViewModel.selectedGrades.value?.let {
            gradesListAdapter.setSelectedGrades(it)
        }
    }

    private fun fetchValidGrades() {
        //TODO: Make "grade -> grade status" validations here
        registerViewModel.generatedRegularGrades.value?.let {
            gradesListAdapter.replaceGrades(it)
        }
    }

    private fun setupListeners() {
        imageView_grade_back.setOnClickListener { activity?.onBackPressed() }
        button_grade_saveButton.setOnClickListener {
            val selectedTags = gradesListAdapter.getSelectedGrades()
            registerViewModel.setSelectedGrades(selectedTags)
            activity?.onBackPressed()
        }
    }

    private fun setupAnnotationTagListAdapter() {
        context?.let {
            gradesListAdapter.setTypeFaces(
                ResourcesCompat.getFont(it, R.font.rubik_medium),
                ResourcesCompat.getFont(it, R.font.rubik)
            )
        }
    }

    private fun initRecyclerView() {
        val mLayoutManager = LinearLayoutManager(activity)
        setupAnnotationTagListAdapter()
        with(recyclerView_grade_list) {
            adapter = gradesListAdapter
            layoutManager = mLayoutManager
        }
        recyclerView_grade_list.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (mLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                        constraintLayout_grade_header.elevation = 8F
                    } else {
                        constraintLayout_grade_header.elevation = 0F
                    }
                }
            })

    }

}