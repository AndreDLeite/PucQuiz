package com.example.pucquiz.ui.quizprogress.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pucquiz.R
import com.example.pucquiz.extensios.overrideOnBackPressed
import com.example.pucquiz.ui.quizprogress.viewmodels.QuizMainViewModel
import kotlinx.android.synthetic.main.fragment_quiz_finish.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class QuizFinishFragment : Fragment() {

    private val quizConfigurationViewModel by sharedViewModel<QuizMainViewModel>()
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_quiz_finish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideOnBackPressed()
        setupPointsInformation()
        calculateUserResults()
        setupQuizGrade()
        setupListeners()
    }

    private fun setupListeners() {
        materialButton_go_back.setOnClickListener {
            quizConfigurationViewModel.clearViewModel()
            listener?.onBackToOnBoarding()
        }
    }

    private fun calculateUserResults() {
        with(quizConfigurationViewModel.getCorrectAmount()) {
            when(this) {
                1 -> {
                    textView_results_title.text = "Podemos melhorar!"
                    textView_results_description.text = "Estude mais sobre a matéria em questão, você consegue se sair bem melhor!"
                    imageView_star_1.visibility = View.VISIBLE
                    imageView_star_2.visibility = View.GONE
                    imageView_star_3.visibility = View.GONE
                    imageView_star_4.visibility = View.GONE
                    imageView_star_5.visibility = View.GONE
                }
                2 -> {
                    textView_results_title.text = "Não tão ruim!"
                    textView_results_description.text = "Mas também ainda abaixo da média! Foque mais seus estudos nessa matéria para melhorar seus resutados!"
                    imageView_star_1.visibility = View.VISIBLE
                    imageView_star_2.visibility = View.VISIBLE
                    imageView_star_3.visibility = View.GONE
                    imageView_star_4.visibility = View.GONE
                    imageView_star_5.visibility = View.GONE
                }
                3 -> {
                    textView_results_title.text = "Na média!"
                    textView_results_description.text = "Muito bom, mas ainda há muito espaço para aprendizado, vamos lá que você consegue!"
                    imageView_star_1.visibility = View.VISIBLE
                    imageView_star_2.visibility = View.VISIBLE
                    imageView_star_3.visibility = View.VISIBLE
                    imageView_star_4.visibility = View.GONE
                    imageView_star_5.visibility = View.GONE
                }
                4 -> {
                    textView_results_title.text = "Podemos melhorar!"
                    textView_results_description.text = "Ainda não esta perfeito, porém com mais estudos vocë conseguirá ir mais além!"
                    imageView_star_1.visibility = View.VISIBLE
                    imageView_star_2.visibility = View.VISIBLE
                    imageView_star_3.visibility = View.VISIBLE
                    imageView_star_4.visibility = View.VISIBLE
                    imageView_star_5.visibility = View.GONE
                }
                5 -> {
                    textView_results_title.text = "Genial!"
                    textView_results_description.text = "Parabéns! Você acertou todas as questões! Continue assim!"
                    imageView_star_1.visibility = View.VISIBLE
                    imageView_star_2.visibility = View.VISIBLE
                    imageView_star_3.visibility = View.VISIBLE
                    imageView_star_4.visibility = View.VISIBLE
                    imageView_star_5.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupPointsInformation() {
        textView_results_quizpoints.text = quizConfigurationViewModel.getUserScore().toString()
    }

    private fun setupQuizGrade() {
        textView_results_quiztype.text = quizConfigurationViewModel.getQuizGrade().name
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    private fun overrideOnBackPressed() {
        activity?.overrideOnBackPressed(viewLifecycleOwner) {
            //TODO: Make logic of back pressed here.
        }
    }

    interface OnFragmentInteractionListener {
        fun onBackToOnBoarding()
    }

}