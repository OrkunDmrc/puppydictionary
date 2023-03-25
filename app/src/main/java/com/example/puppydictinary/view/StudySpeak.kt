package com.example.puppydictinary.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import kotlinx.android.synthetic.main.fragment_study_selection.*
import kotlinx.android.synthetic.main.fragment_study_speak.*
import kotlinx.android.synthetic.main.fragment_study_speak.description_layout
import kotlinx.android.synthetic.main.fragment_study_speak.nextButton
import kotlinx.android.synthetic.main.fragment_study_speak.phonetic_layout
import kotlinx.android.synthetic.main.fragment_study_speak.phonetic_text
import kotlinx.android.synthetic.main.fragment_study_speak.scroll_view
import kotlinx.android.synthetic.main.fragment_study_speak.word_text
import java.util.*


class StudySpeak(val wordsList: ArrayList<WordViewModel>, val studyImageViews: ArrayList<ImageView>, val reportList: ArrayList<ArrayList<Boolean>>) : Fragment() {
    private var wordIndex: Int = -1
    var tts: TextToSpeech? = null
    private var booleanList = arrayListOf<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_study_speak, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studyImageViews[0].setBackgroundResource(R.color.button)
        studyImageViews[1].setBackgroundResource(R.color.button)
        studyImageViews[2].setBackgroundResource(R.color.card)
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, MainActivity.findSpeechVoiceById(wordsList[0].LangId))
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(i: Int) {}
            override fun onResults(bundle: Bundle) {
                val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result != null) {
                    if(result[0] == wordsList[wordIndex].Word){
                        mic_button_background.setBackgroundResource(R.color.button)
                        booleanList.add(true)
                    }else{
                        mic_button_background.setBackgroundResource(R.color.wrong)
                        booleanList.add(false)
                    }
                    speech_text.text = result[0]
                    mic_button.isEnabled = false
                    description_layout.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                    mic_button.isEnabled = false
                }
            }
            override fun onPartialResults(bundle: Bundle) {}
            override fun onEvent(i: Int, bundle: Bundle?) {}
        })
        mic_button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    checkAudioPermission()
                    mic_button_background.setBackgroundResource(R.color.background)
                    speechRecognizer.startListening(speechRecognizerIntent)
                }
                MotionEvent.ACTION_UP -> {
                    mic_button_background.setBackgroundResource(R.color.card)
                    speechRecognizer.stopListening()
                }
            }
            true
        }
        phonetic_layout.setOnClickListener{
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = MainActivity.findLangById(wordsList[wordIndex].LangId)
                    tts?.setSpeechRate(1.0f)
                    tts?.speak(wordsList[wordIndex].Word, TextToSpeech.QUEUE_ADD, null,"")
                }
            })
        }
        mic_button.setOnClickListener {
            checkAudioPermission()
        }
        nextButton.setOnClickListener {
            if(wordIndex < wordsList.size - 1) {
                fillObjects()
            }else {
                reportList.add(booleanList)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.study_frame, StudyReport(wordsList, studyImageViews, reportList)).commit()
            }
            nextButton.visibility = View.INVISIBLE
            description_layout.visibility = View.INVISIBLE
            scroll_view.fullScroll(ScrollView.FOCUS_UP)
        }
        not_speak.setOnClickListener {
            for(i in booleanList.size - 1 until wordsList.size){
                booleanList.add(false)
            }
            reportList.add(booleanList)
            parentFragmentManager.beginTransaction()
                .replace(R.id.study_frame, StudyReport(wordsList, studyImageViews, reportList)).commit()
        }
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
        description_layout.visibility = View.INVISIBLE
        nextButton.visibility = View.INVISIBLE
        mic_button.isEnabled = true
        mic_button_background.setBackgroundResource(R.color.card)
        word_text.text = wordsList[wordIndex].Word
        phonetic_text.text = wordsList[wordIndex].Phonetic
        description_text.text = wordsList[wordIndex].Description
        speech_text.text = null
    }

    fun checkAudioPermission() {
        if(ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(android.Manifest.permission.RECORD_AUDIO),1);
        }
    }


}