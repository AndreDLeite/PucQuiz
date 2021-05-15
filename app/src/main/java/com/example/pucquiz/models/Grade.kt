package com.example.pucquiz.models

data class Grade(
    val gradeType: String = "",
    val period: Int = -1,
    var isAvailable: Boolean = false
)

enum class GradeEnum {
//    AEDI,
//    AEDII,
//    DISPOSITIVOS_MOVEIS,
//    REDESI,
    REDESII,
//    COMPUTACAO_PARALELA,
//    OTIMIZACAO,
//    GRAFOS,
    UNKOWN
}