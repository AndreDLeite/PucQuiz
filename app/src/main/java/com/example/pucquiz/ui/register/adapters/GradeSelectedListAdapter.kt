package com.example.pucquiz.ui.register.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.pucquiz.R
import kotlinx.android.synthetic.main.card_grade_registration.view.*

class GradeSelectedListAdapter(private var gradesList: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListener: OnInteractionClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnnotationSelectedTagViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_grade_registration,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            with(holder as AnnotationSelectedTagViewHolder) {
                holder.gradeName.text = gradesList[position]
                this.itemView.setOnClickListener { mListener?.onRecyclerViewItemClicked() }
            }

        } catch (t: Throwable) {
            t.printStackTrace()
        }

    }

    fun setAnnotationTagAdapterListener(listener: OnInteractionClickListener) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return gradesList.size
    }

    fun replaceTags(gradeNames: List<String>) {
        gradesList = gradeNames
        notifyDataSetChanged()
    }

    interface OnInteractionClickListener {
        fun onRecyclerViewItemClicked()
    }

    class AnnotationSelectedTagViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val gradeName = itemView.textView_register_grade_selected_name
    }
}