package com.example.pucquiz.ui.quizhall.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.ui.quizhall.viewmodels.QuestionsHallViewModel
import org.koin.android.ext.android.inject

class QuizHallFragment : Fragment() {

    private val questionsHallViewModel by inject<QuestionsHallViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_quiz_hall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionsHallViewModel.fetchTeacherQuestions()
        overrideOnBackPressed()
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }

}