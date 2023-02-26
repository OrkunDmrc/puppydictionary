package com.example.puppydictinary.adapter

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import kotlinx.android.synthetic.main.study_report_recycler_row.view.*
import java.util.*
import kotlin.collections.ArrayList

class StudyReportRecyclerAdapter(var wordsList: ArrayList<WordViewModel>, val context: Context, val view: View) : RecyclerView.Adapter<StudyReportRecyclerAdapter.StudyReportVH>(){
    class StudyReportVH(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyReportVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.study_report_recycler_row, parent,false)
        return StudyReportVH(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: StudyReportVH, position: Int) {
        var tts: TextToSpeech? = null
        val getPosition = wordsList.get(position)
        val holderItemView = holder.itemView
        holderItemView.word_text.text = getPosition.Word
        holderItemView.phonetic_text.text = getPosition.Phonetic
        holderItemView.description_text.text = getPosition.Description
        holderItemView.pronunciation_button.setOnClickListener {
            holderItemView.pronunciation_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_volume_down_24)
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.setSpeechRate(1.0f)
                    tts?.speak(getPosition.Word, TextToSpeech.QUEUE_ADD, null,"")
                }
                holderItemView.pronunciation_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_volume_up_24)
            })
        }

    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

}