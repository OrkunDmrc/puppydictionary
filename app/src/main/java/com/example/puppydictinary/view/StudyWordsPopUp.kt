package com.example.puppydictinary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import kotlinx.android.synthetic.main.fragment_study_words_pop_up.*
import kotlin.random.Random


class StudyWordsPopUp(val words: List<WordViewModel>, val navController: NavController, val myLang: String, val learningLang: String) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_study_words_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var maxValue = if(words.size >= 20) 20 else words.size
        for(i in 5..maxValue step 5){
            val word_number_button = Button(context)
            word_number_button.layoutParams = RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val layoutParams = word_number_button.layoutParams as RelativeLayout.LayoutParams
            layoutParams.setMargins(5,5,5,5)
            word_number_button.text = "${getString(R.string.popupBeforeNumber)} ${i} ${getString(R.string.popupAfterNumber)}"
            word_number_button.setBackgroundColor(getResources().getColor(R.color.background))
            word_number_button.setTextColor(getResources().getColor(R.color.text))
            word_number_button.setTextSize(2,18F)
            word_number_button.setOnClickListener{
                dismiss()
                var wordIds = ArrayList<Int>()
                val randomValues = List(i) { Random.nextInt(0, words.size - 1) }
                for(item in randomValues){
                    wordIds.add(words[item].Id)
                }
                val action = FavoriteWordsListDirections.actionFavoriteWordsListToStudy(wordIds.toIntArray(), myLang, learningLang)
                navController.navigate(action)
            }
            linear_layout.addView(word_number_button)
        }

    }

}