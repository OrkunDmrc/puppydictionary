package com.example.puppydictinary.view

import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import kotlinx.android.synthetic.main.fragment_desc_pop_up.view.*
import kotlinx.android.synthetic.main.fragment_study_selection.*
import java.util.*
import kotlin.collections.ArrayList

class StudySelection(val wordsList: List<WordViewModel>) : Fragment() {
    private var wordIndex: Int = 0
    private var correctIndex: Int = 0
    private lateinit var optionList: ArrayList<TextView>
    var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_study_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        optionList = arrayListOf<TextView>(option_1, option_2, option_3, option_4)
        nextButton.setOnClickListener {
            println("will be next")
        }
        nextButton.visibility = View.INVISIBLE
        fillObjects()
        for(item in optionList){
            item.setOnClickListener{
                if(correctIndex == optionList.indexOf(item) || optionList[correctIndex].text == wordsList[wordIndex].Description){
                    optionList[optionList.indexOf(item)].setBackgroundResource(R.color.button)
                }else{
                    optionList[correctIndex].setBackgroundResource(R.color.button)
                    optionList[optionList.indexOf(item)].setBackgroundResource(R.color.wrong)
                }
                Handler().postDelayed({
                    wordIndex++
                    if(wordIndex < wordsList.size) fillObjects() else nextButton.visibility = View.VISIBLE
                }, 1500)
            }
        }
    }

    private fun fillObjects(){
        word_text.text = wordsList[wordIndex].Word
        phonetic_text.text = wordsList[wordIndex].Phonetic
        for(item in optionList){
            item.text = wordsList[(wordsList.indices).random()].Description
            item.setBackgroundResource(R.color.card)
        }
        correctIndex = (0..3).random()
        optionList[correctIndex].text = wordsList[wordIndex].Description
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(1.0f)
                tts?.speak(wordsList[wordIndex].Word, TextToSpeech.QUEUE_ADD, null,"")
            }
        })
        phonetic_layout.setOnClickListener {
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.setSpeechRate(1.0f)
                    tts?.speak(wordsList[wordIndex].Word, TextToSpeech.QUEUE_ADD, null,"")
                }
            })
        }
    }



}