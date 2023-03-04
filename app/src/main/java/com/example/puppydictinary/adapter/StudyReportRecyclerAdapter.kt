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
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel
import kotlinx.android.synthetic.main.fragment_main_menu.*
import kotlinx.android.synthetic.main.study_report_recycler_row.view.*
import java.util.*
import kotlin.collections.ArrayList

class StudyReportRecyclerAdapter(var wordsList: ArrayList<WordViewModel>, val reportList: ArrayList<ArrayList<Boolean>>, val context: Context, val view: View, val favoriteWordsListViewModel: FavoriteWordsListViewModel) : RecyclerView.Adapter<StudyReportRecyclerAdapter.StudyReportVH>(){
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
        holderItemView.description_text.text = getPosition.Description.replaceFirst("/n","")
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
        holderItemView.add_remove_favorite_button.setOnClickListener {
            if(favoriteWordsListViewModel.isFavoriteWord(getPosition.Word)){
                favoriteWordsListViewModel.removeWordFavorites(getPosition.Word)
                holderItemView.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
            }else{
                favoriteWordsListViewModel.addRecordedWordFavorites(getPosition.Word)
                holderItemView.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
            }
        }
        for (i in 0 until 3){
            when(i){
                0 ->
                    if(reportList[i][position]){
                        holderItemView.selection_study.setBackgroundResource(R.color.button)
                    }else{
                        holderItemView.selection_study.setBackgroundResource(R.color.wrong)
                    }
                1 ->
                    if(reportList[i][position]){
                        holderItemView.voice_study.setBackgroundResource(R.color.button)
                    }else{
                        holderItemView.voice_study.setBackgroundResource(R.color.wrong)
                    }
                2 ->
                    if(reportList[i][position]){
                        holderItemView.pronunciation_study.setBackgroundResource(R.color.button)
                    }else{
                        holderItemView.pronunciation_study.setBackgroundResource(R.color.wrong)
                    }
                else -> println("We have a problem here")
            }
        }
    }
    override fun getItemCount(): Int {
        return wordsList.size
    }

}