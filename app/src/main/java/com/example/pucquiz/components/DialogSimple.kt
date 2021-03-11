package com.example.pucquiz.components

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.example.pucquiz.R
import kotlinx.android.synthetic.main.dialog_simple.*

class DialogSimple : DialogFragment() {

    private var mTitle: String = ""
    private var mDescription: String = ""
    private var mBoldText: String = ""
    private var mConfirmText: String = "Ok"
    private var mCancelText: String? = null
    private var mWarning: String? = null
    private var mConfirmTextColor: Int? = null
    private var mWarningTextColor: Int? = null
    private var mIsCancelable: Boolean = true

    private var mListener: SimpleDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCancelButton()
        setupConfirmButton()
        setupTextViews()
        setupIsCancelable()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setupIsCancelable() {
        isCancelable = mIsCancelable
    }

    private fun setupTextViews() {
        textView_dialogSimple_title.text = mTitle

        val boldText = mBoldText
        val originalText = mDescription
        val spannableString = SpannableString(originalText)
        if (originalText.contains(boldText)) {
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                originalText.indexOf(boldText),
                originalText.indexOf(boldText) + boldText.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        textView_dialogSimple_description.text = spannableString

        if (mWarning != null) {
            textView_dialog_warning.text = mWarning
            textView_dialog_warning.visibility = View.VISIBLE
            mWarningTextColor?.let {
                textView_dialog_warning.setTextColor(it)
            }
        }
    }

    private fun setupConfirmButton() {
        button_dialog_confirmButton.text = mConfirmText
        mConfirmTextColor?.let { button_dialog_confirmButton.setTextColor(it) }
        button_dialog_confirmButton.setOnClickListener {
            mListener?.onDialogPositiveClick(
                this
            )
        }
    }

    private fun setupCancelButton() {
        if (mCancelText != null) {
            button_dialog_cancelButton.text = mCancelText
            button_dialog_cancelButton.setOnClickListener {
                mListener?.onDialogNegativeClick(
                    this
                )
            }
        } else {
            button_dialog_cancelButton.visibility = View.GONE
        }
    }

    fun setDialogParameters(
        title: String,
        description: String,
        warning: String? = null,
        confirmText: String,
        cancelText: String?,
        confirmTextColor: Int? = null,
        warningTextColor: Int? = null,
        boldText: String = "",
        isCancelable: Boolean = true
    ) {
        mTitle = title
        mDescription = description
        mBoldText = boldText
        mWarning = warning
        mConfirmText = confirmText
        mCancelText = cancelText
        mConfirmTextColor = confirmTextColor
        mWarningTextColor = warningTextColor
        mIsCancelable = isCancelable
    }

    fun setDialogListener(@NonNull listener: SimpleDialogListener) {
        mListener = listener
    }

    interface SimpleDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }
}