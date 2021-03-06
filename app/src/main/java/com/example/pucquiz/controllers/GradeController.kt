package com.example.pucquiz.controllers

import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.GradeEnum

class GradeController {

    fun generateBasedOnPeriodGrades(userPeriod: Int): List<Grade> {
        val gradeList = mutableListOf<Grade>()
        GradeEnum.values().forEach {
            if(it == GradeEnum.UNKOWN) return@forEach
            val grade = gradeToPeriod(it)
            gradeList.add(
                Grade(
                    gradeType = gradeToString(it),
                    period = grade,
                    isAvailable = grade <= userPeriod
                )
            )
        }
        return gradeList
    }

    private fun gradeToPeriod(enum: GradeEnum): Int {
        return when (enum) {
//            GradeEnum.AEDI -> {
//                1
//            }
//            GradeEnum.AEDII -> {
//                2
//            }
//            GradeEnum.DISPOSITIVOS_MOVEIS -> {
//                2
//            }
//            GradeEnum.REDESI -> {
//                3
//            }
            GradeEnum.REDESII -> {
                3
            }
//            GradeEnum.COMPUTACAO_PARALELA -> {
//                4
//            }
//            GradeEnum.OTIMIZACAO -> {
//                4
//            }
//            GradeEnum.GRAFOS -> {
//                5
//            }
            else -> {
                0
            }
        }
    }

    fun gradeFromString(gradeString: String): GradeEnum {
        return when (gradeString) {
//            "AEDS I" -> {
//                GradeEnum.AEDI
//            }
//            "AEDS II" -> {
//                GradeEnum.AEDII
//            }
//            "Dispositivos Móveis" -> {
//                GradeEnum.DISPOSITIVOS_MOVEIS
//            }
//            "Redes I" -> {
//                GradeEnum.REDESI
//            }
            "Redes II" -> {
                GradeEnum.REDESII
            }
//            "Computação Paralela" -> {
//                GradeEnum.COMPUTACAO_PARALELA
//            }
//            "Otmização" -> {
//                GradeEnum.OTIMIZACAO
//            }
//            "Gráfos" -> {
//                GradeEnum.GRAFOS
//            }
            else -> {
                GradeEnum.UNKOWN
            }
        }
    }

    fun gradeToString(enum: GradeEnum): String {
        return when (enum) {
//            GradeEnum.AEDI -> {
//                "AEDS I"
//            }
//            GradeEnum.AEDII -> {
//                "AEDS II"
//            }
//            GradeEnum.DISPOSITIVOS_MOVEIS -> {
//                "Dispositivos Móveis"
//            }
//            GradeEnum.REDESI -> {
//                "Redes I"
//            }
            GradeEnum.REDESII -> {
                "Redes II"
            }
//            GradeEnum.COMPUTACAO_PARALELA -> {
//                "Computação Paralela"
//            }
//            GradeEnum.OTIMIZACAO -> {
//                "Otmização"
//            }
//            GradeEnum.GRAFOS -> {
//                "Gráfos"
//            }
            else -> {
                ""
            }
        }
    }

}