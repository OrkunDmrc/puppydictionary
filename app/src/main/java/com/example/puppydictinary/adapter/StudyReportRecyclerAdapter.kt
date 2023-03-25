package com.example.puppydictinary.adapter

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import com.example.puppydictinary.view.MainActivity
import com.example.puppydictinary.viewmodel.FavoriteWordsListViewModel
import kotlinx.android.synthetic.main.study_report_recycler_row.view.*
import kotlinx.android.synthetic.main.study_report_recycler_row.view.add_remove_favorite_button
import kotlinx.android.synthetic.main.study_report_recycler_row.view.add_remove_learned_button
import kotlinx.android.synthetic.main.study_report_recycler_row.view.phonetic_text
import kotlinx.android.synthetic.main.study_report_recycler_row.view.pronunciation_button
import kotlinx.android.synthetic.main.study_report_recycler_row.view.word_text
import kotlin.collections.ArrayList

class StudyReportRecyclerAdapter(var wordsList: ArrayList<WordViewModel>, val reportList: ArrayList<ArrayList<Boolean>>, val context: Context, val view: View, val favoriteWordsListViewModel: FavoriteWordsListViewModel) : RecyclerView.Adapter<StudyReportRecyclerAdapter.StudyReportVH>(){
    class StudyReportVH(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
    private var viewHolderList = ArrayList<View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyReportVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.study_report_recycler_row, parent,false)
        return StudyReportVH(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: StudyReportVH, position: Int) {
        var tts: TextToSpeech? = null
        val getPosition = wordsList[position]
        val holderItemView = holder.itemView
        viewHolderList.add(holderItemView)
        holderItemView.word_text.text = getPosition.Word
        holderItemView.phonetic_text.text = getPosition.Phonetic
        holderItemView.description_text.text = getPosition.Description.replaceFirst("\n","")
        holderItemView.pronunciation_button.setOnClickListener {
            holderItemView.pronunciation_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_volume_down_24)
            tts = TextToSpeech(context, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = MainActivity.findLangById(getPosition.LangId)
                    tts?.setSpeechRate(1.0f)
                    tts?.speak(getPosition.Word, TextToSpeech.QUEUE_ADD, null,"")
                }
                holderItemView.pronunciation_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_volume_up_24)
            })
        }
        if(getPosition.IsLearned == 1){
            holderItemView.add_remove_learned_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_24)
        }else{
            holderItemView.add_remove_learned_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_border_24)
        }
        if(getPosition.IsFav == 1){
            holderItemView.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
        }else{
            holderItemView.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
        }
        holderItemView.add_remove_learned_button.setOnClickListener {
            if(getPosition.IsLearned == 1){
                favoriteWordsListViewModel.removeWordLearned(getPosition.Word)
                setAsNotLearned(getPosition.Word)
            }else{
                favoriteWordsListViewModel.addRecordedWordLearned(getPosition.Word)
                setAsLearned(getPosition.Word)
            }
        }
        holderItemView.add_remove_favorite_button.setOnClickListener {
            if(getPosition.IsFav == 1){
                favoriteWordsListViewModel.removeWordFavorites(getPosition.Word)
                setAsNotFav(getPosition.Word)
            }else{
                favoriteWordsListViewModel.addRecordedWordFavorites(getPosition.Word)
                setAsFav(getPosition.Word)
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
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAsNotLearned(word: String){
        for(item in viewHolderList.filter { v -> v.word_text.text == word }){
           item.add_remove_learned_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_border_24)
        }
        for(item in wordsList.filter { w -> w.Word == word }){
            item.IsLearned = 0
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAsLearned(word: String){
        for(item in viewHolderList.filter { v -> v.word_text.text == word }){
            item.add_remove_learned_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_24)
        }
        for(item in wordsList.filter { w -> w.Word == word }){
            item.IsLearned = 1
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAsNotFav(word: String){
        for(item in viewHolderList.filter { v -> v.word_text.text == word }){
            item.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
        }
        for(item in wordsList.filter { w -> w.Word == word }){
            item.IsFav = 0
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAsFav(word: String){
        for(item in viewHolderList.filter { v -> v.word_text.text == word }){
            item.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
        }
        for(item in wordsList.filter { w -> w.Word == word }){
            item.IsFav = 1
        }
    }

}