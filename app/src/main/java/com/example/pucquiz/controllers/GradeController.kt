package com.example.pucquiz.controllers

import com.example.pucquiz.models.Grade
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.GradeStatus

class GradeController {
    fun generateGradesByStatusAndPeriod(gradeStatus: GradeStatus, period: Int): List<String> {
        return when(gradeStatus) {
            GradeStatus.REGULAR -> {
                generateGradesByPeriod(period)
            }
            GradeStatus.IRREGULAR -> {
                emptyList()
            }
        }
    }

    private fun generateGradesByPeriod(period: Int): List<String> {
        return when(period) {
            1 -> {
                generateFistGradeClasses()
            }
            2 -> {
                generateSecondGradeClasses()
            }
            else -> {
                emptyList()
            }
        }
    }

    private fun generateFistGradeClasses(): List<String> {
        return listOf(
            Grade().gradeToString(GradeEnum.AEDI),
            Grade().gradeToString(GradeEnum.REDESI)
        )
    }

    private fun generateSecondGradeClasses(): List<String> {
        return listOf(
            Grade().gradeToString(GradeEnum.AEDII)
        )
    }
}