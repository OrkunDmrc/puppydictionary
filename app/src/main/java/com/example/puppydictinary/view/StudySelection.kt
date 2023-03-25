package com.example.puppydictinary.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import kotlinx.android.synthetic.main.fragment_study_selection.*
import kotlin.collections.ArrayList

class StudySelection(val wordsList: ArrayList<WordViewModel>, val studyImageViews: ArrayList<ImageView>) : Fragment() {
    private var wordIndex: Int = -1
    private var correctIndex: Int = 0
    private lateinit var optionList: ArrayList<TextView>
    private var reportList = arrayListOf<ArrayList<Boolean>>()
    private var booleanList = arrayListOf<Boolean>()
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
        studyImageViews[0].setBackgroundResource(R.color.card)
        optionList = arrayListOf<TextView>(option_1, option_2, option_3, option_4)
        phonetic_layout.setOnClickListener {
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = MainActivity.findLangById(wordsList[wordIndex].LangId)
                    tts?.setSpeechRate(1.0f)
                    tts?.speak(wordsList[wordIndex].Word, TextToSpeech.QUEUE_ADD, null,"")
                }
            })
        }
        nextButton.setOnClickListener {
            if(wordIndex < wordsList.size - 1){
                fillObjects()
            }
            else{
                reportList.add(booleanList)
                parentFragmentManager.beginTransaction().replace(R.id.study_frame, StudyVoice(wordsList, studyImageViews, reportList)).commit()
            }
            nextButton.visibility = View.INVISIBLE
            scroll_view.fullScroll(ScrollView.FOCUS_UP)
        }
        nextButton.visibility = View.INVISIBLE
        fillObjects()
        for(item in optionList){
            item.setOnClickListener{
                if(correctIndex == optionList.indexOf(item) || optionList[correctIndex].text == item.text){
                    optionList[optionList.indexOf(item)].setBackgroundResource(R.color.button)
                    booleanList.add(true)
                }else{
                    optionList[correctIndex].setBackgroundResource(R.color.button)
                    optionList[optionList.indexOf(item)].setBackgroundResource(R.color.wrong)
                    booleanList.add(false)
                }
                nextButton.visibility = View.VISIBLE
                makeDisenable()
            }
        }
    }

    private fun fillObjects(){
        wordIndex++
        println(wordIndex)
        makeEnable()
        word_text.text = wordsList[wordIndex].Word
        phonetic_text.text = wordsList[wordIndex].Phonetic
        for(item in optionList){
            item.text = wordsList[(wordsList.indices).random()].Description.replace("\n", " ")
            item.setBackgroundResource(R.color.card)
        }
        correctIndex = (0..3).random()
        optionList[correctIndex].text = wordsList[wordIndex].Description.replace("\n", " ")
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = MainActivity.findLangById(wordsList[wordIndex].LangId)
                tts?.setSpeechRate(1.0f)
                tts?.speak(wordsList[wordIndex].Word, TextToSpeech.QUEUE_ADD, null,"")
            }
        })
    }

    private fun makeEnable(){
        for (item in optionList){
            item.isEnabled = true
        }
    }

    private fun makeDisenable(){
        for (item in optionList){
            item.isEnabled = false
        }
    }



}