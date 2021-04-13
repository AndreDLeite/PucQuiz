package com.example.pucquiz.ui.register.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import com.example.pucquiz.models.Grade
import kotlinx.android.synthetic.main.item_grade_selection.view.*

class GradeListAdapter(
    private var gradeList: List<Grade>,
    private var selectedGrades: MutableList<Grade>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedTypeface: Typeface? = null
    private var unselectedTypeface: Typeface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GradeItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_grade_selection,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as GradeItemViewHolder) {
            val currentGrade = gradeList[position]
            val gradeName = currentGrade.gradeType
            gradeCheckBox.text = gradeName
            if (currentGrade.isAvailable) {

                if (selectedGrades.contains(currentGrade)) {
                    selectedTypeface?.let { holder.gradeCheckBox.typeface = it }
                } else {
                    unselectedTypeface?.let { holder.gradeCheckBox.typeface = it }
                }
                gradeCheckBox.isChecked = selectedGrades.contains(currentGrade)
                gradeCheckBox.setOnClickListener {
                    toggleTag(currentGrade)
                }
            } else {
                holder.gradeCheckBox.isEnabled = false
            }
        }


    }

    fun setTypeFaces(
        selectedTypeface: Typeface?,
        unselectedTypeface: Typeface?
    ) {
        this.selectedTypeface = selectedTypeface
        this.unselectedTypeface = unselectedTypeface
    }

    fun setSelectedGrades(selectedTags: List<Grade>) {
        selectedGrades.clear()
        selectedGrades.addAll(selectedTags)
        notifyDataSetChanged()
    }

    private fun toggleTag(grade: Grade) {
        if (selectedGrades.contains(grade)) {
            selectedGrades.remove(grade)
        } else {
            selectedGrades.add(grade)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return gradeList.size
    }

    fun getSelectedGrades() = selectedGrades

    fun replaceGrades(grades: List<Grade>) {
        gradeList = grades
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class GradeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gradeCheckBox = itemView.checkBox_grade_item
    }
}