package com.example.puppydictinary.view

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ScrollView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import kotlinx.android.synthetic.main.fragment_study_voice.*
import kotlinx.android.synthetic.main.fragment_study_voice.description_layout
import kotlinx.android.synthetic.main.fragment_study_voice.description_text
import kotlinx.android.synthetic.main.fragment_study_voice.nextButton
import kotlinx.android.synthetic.main.fragment_study_voice.word_text
import kotlin.collections.ArrayList

class StudyVoice(val wordsList: ArrayList<WordViewModel>, val studyImageViews: ArrayList<ImageView>, val reportList: ArrayList<ArrayList<Boolean>>) : Fragment() {
    private var wordIndex: Int = -1
    var tts: TextToSpeech? = null
    private var booleanList = arrayListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_study_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studyImageViews[0].setBackgroundResource(R.color.button)
        studyImageViews[1].setBackgroundResource(R.color.card)
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        voice_button.setOnClickListener{
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = MainActivity.findLangById(wordsList[wordIndex].LangId)
                    tts?.setSpeechRate(1.0f)
                    tts?.speak(wordsList[wordIndex].Word, TextToSpeech.QUEUE_ADD, null,"")
                }
            })
        }
        check_button.setOnClickListener {
            println(word_text.text.trim())
            println(wordsList[wordIndex].Word.trim())
            println(word_text.text.toString().trim() == wordsList[wordIndex].Word.trim())
            if(word_text.text.toString().trim() == wordsList[wordIndex].Word.trim()){
                word_text.setBackgroundResource(R.color.button)
                booleanList.add(true)
            }else{
                word_text.setBackgroundResource(R.color.wrong)
                booleanList.add(false)
            }
            imm.hideSoftInputFromWindow(view?.getWindowToken(), 0)
            check_button.isEnabled = false
            description_layout.visibility = View.VISIBLE
            nextButton.visibility = View.VISIBLE
        }
        nextButton.setOnClickListener {
            if(wordIndex < wordsList.size - 1){
                fillObjects()

            }
            else{
                reportList.add(booleanList)
                parentFragmentManager.beginTransaction().replace(R.id.study_frame, StudySpeak(wordsList, studyImageViews, reportList)).commit()
            }
            nextButton.visibility = View.INVISIBLE
            description_layout.visibility = View.INVISIBLE
            scroll_view.fullScroll(ScrollView.FOCUS_UP)
        }
        nextButton.visibility = View.INVISIBLE
        description_layout.visibility = View.INVISIBLE
        fillObjects()
    }

    private fun fillObjects(){
        wordIndex++
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = MainActivity.findLangById(wordsList[wordIndex].LangId)
                tts?.setSpeechRate(1.0f)
                tts?.speak(wordsList[wordIndex].Word, TextToSpeech.QUEUE_ADD, null,"")
            }
        })
        check_button.isEnabled = true
        des_word_text.text = wordsList[wordIndex].Word
        button_phonetic_text.text = wordsList[wordIndex].Phonetic
        phonetic_text_2.text = wordsList[wordIndex].Phonetic
        description_text.text = wordsList[wordIndex].Description
        word_text.text = null
        word_text.setBackgroundResource(R.color.card)
    }

}