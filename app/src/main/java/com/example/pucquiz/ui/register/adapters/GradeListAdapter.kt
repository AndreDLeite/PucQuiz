package com.example.pucquiz.ui.register.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import kotlinx.android.synthetic.main.item_grade_selection.view.*

class GradeListAdapter(
    @NonNull private var gradeList: List<String>,
    @NonNull private var selectedGrades: MutableList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSelectedTypeface: Typeface? = null
    private var mUnselectedTypeface: Typeface? = null

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
            val tagName = gradeList[position]
            gradeCheckBox.text = tagName
            if (selectedGrades.contains(tagName)) {
                mSelectedTypeface?.let { holder.gradeCheckBox.typeface = it }

            } else {
                mUnselectedTypeface?.let { holder.gradeCheckBox.typeface = it }
            }
            gradeCheckBox.isChecked = selectedGrades.contains(tagName)
            gradeCheckBox.setOnClickListener {
                toggleTag(tagName)
            }
        }


    }

    fun setTypeFaces(selectedTypeface: Typeface?, unselectedTypeface: Typeface?) {
        mSelectedTypeface = selectedTypeface
        mUnselectedTypeface = unselectedTypeface
    }

    fun setSelectedGrades(selectedTags: List<String>) {
        selectedGrades.clear()
        selectedGrades.addAll(selectedTags)
        notifyDataSetChanged()
    }

    fun toggleTag(tag: String) {
        if (selectedGrades.contains(tag)) {
            selectedGrades.remove(tag)
        } else {
            selectedGrades.add(tag)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return gradeList.size
    }

    fun getSelectedGrades() = selectedGrades

    fun replaceGrades(grades: List<String>) {
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