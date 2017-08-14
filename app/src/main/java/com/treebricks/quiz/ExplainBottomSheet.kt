package com.treebricks.quiz

import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
* Created by fahim on 6/22/17.
* Project: Quiz
*/

class ExplainBottomSheet : BottomSheetDialogFragment() {

    private var correctAnswer: String? = null
    private var explainText: String? = null
    private var explainImage: Int? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.bottom_sheet_main, container, false)

        val correctAnswerTextview = view.findViewById(R.id.correctAnswer) as TextView
        val explainImageView = view.findViewById(R.id.explainImageView) as ImageView
        val explainTextview = view.findViewById(R.id.explainTextView) as TextView

        correctAnswerTextview.text = String.format("%s %s",getString(R.string.correct_answer), correctAnswer)
        explainTextview.text = explainText
        explainImageView.setImageResource(explainImage as Int)

        return view
    }

    fun newInstance(correctAnswer: String, explaination: String, image: Int): ExplainBottomSheet {
        val modalBottomSheet = ExplainBottomSheet()
        modalBottomSheet.correctAnswer = correctAnswer
        modalBottomSheet.explainText = explaination
        modalBottomSheet.explainImage = image
        return modalBottomSheet
    }
}