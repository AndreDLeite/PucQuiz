package com.example.pucquiz.ui.quizselector.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.ui.quizselector.viewmodels.QuizConfigurationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizConfigurationFragment : Fragment() {

    private val quizConfigurationViewModel by sharedViewModel<QuizConfigurationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_question_hall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInitialOnBackPressed()
    }

    private fun setupInitialOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Add onBackPressed action here
        }
    }

}
