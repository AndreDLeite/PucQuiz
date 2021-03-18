package com.example.pucquiz.models


data class Grade(
    val gradeType: String = "",
    val period: Int,
    var isAvailable: Boolean
) {
    fun gradeToString(enum: GradeEnum): String {
        return when(enum) {
            GradeEnum.AEDI -> {
                "AEDS I"
            }
            GradeEnum.AEDII -> {
                "AEDS II"
            }
            GradeEnum.DISPOSITIVOS_MOVEIS -> {
                "Dispositivos Móveis"
            }
            GradeEnum.REDESI -> {
                "Redes I"
            }
            GradeEnum.REDESII -> {
                "Redes II"
            }
            GradeEnum.COMPUTACAO_PARALELA -> {
                "Computação Paralela"
            }
            GradeEnum.OTIMIZACAO -> {
                "Otmização"
            }
            GradeEnum.GRAFOS -> {
                "Gráfos"
            }
        }
    }
}

enum class GradeEnum {
    AEDI,
    AEDII,
    DISPOSITIVOS_MOVEIS,
    REDESI,
    REDESII,
    COMPUTACAO_PARALELA,
    OTIMIZACAO,
    GRAFOS
}