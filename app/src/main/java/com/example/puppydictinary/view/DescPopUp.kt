package com.example.puppydictinary.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import kotlinx.android.synthetic.main.fragment_desc_pop_up.*
import java.util.*


class DescPopUp(val wordViewModel: WordViewModel) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_desc_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var tts: TextToSpeech? = null
        response_text_from_lang.text = wordViewModel.Word
        phonetic_text.text = wordViewModel.Phonetic
        description_text.text = wordViewModel.Description
        pronunciation_button.setOnClickListener{
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.setSpeechRate(1.0f)
                    tts?.speak(wordViewModel.Word, TextToSpeech.QUEUE_ADD, null,"")
                }
            })
        }
    }
}