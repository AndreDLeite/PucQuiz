package com.example.pucquiz.models

import com.example.pucquiz.R

data class Medals(
    val name: String = "",
    val description: String = "",
    val type: MedalsType = MedalsType.UNKNOWN,
    val medalIsActive: Boolean = false
)

enum class MedalsType {
    ONE_QUIZ_ANSWERED {
        override fun resourceColor(): Int = R.color.oneQuizAnswered
    },
    FIVE_QUIZ_ANSWERED {
        override fun resourceColor(): Int = R.color.fiveQuizAnswered
    },
    TEN_QUIZ_ANSWERED {
        override fun resourceColor(): Int = R.color.tenQuizAnswered
    },
    HND_SCORE_REACHED {
        override fun resourceColor(): Int = R.color.oneHundredScore
    },
    THF_SCORE_REACHED {
        override fun resourceColor(): Int = R.color.twoHundredFiftyScore
    },
    FH_SCORE_REACHED {
        override fun resourceColor(): Int = R.color.fiveHundredScore
    },
    FIRST_RANKING_POSITION {
        override fun resourceColor(): Int = R.color.firstRanking
    },
    UNKNOWN {
        override fun resourceColor(): Int = R.color.primaryText
    };

    abstract fun resourceColor(): Int
}
