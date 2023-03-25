package com.example.puppydictinary.adapter

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.WordViewModel
import com.example.puppydictinary.view.DescPopUp
import com.example.puppydictinary.view.MainActivity
import com.example.puppydictinary.viewmodel.LearnedWordListViewModel
import kotlinx.android.synthetic.main.favorite_words_list_recycler_row.view.*
import kotlin.collections.ArrayList

class LearnedWordListRecyclerAdapter(var wordsList: ArrayList<WordViewModel>, val context: Context, private val activity: AppCompatActivity, val learnedWordsListViewModel: LearnedWordListViewModel) : RecyclerView.Adapter<LearnedWordListRecyclerAdapter.LearnedWordsListVH>() {
    class LearnedWordsListVH(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnedWordsListVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.learned_word_list_recycler_row, parent,false)
        return LearnedWordsListVH(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: LearnedWordsListVH, position: Int) {
        var tts: TextToSpeech? = null
        val getPosition = wordsList.get(position)
        val holderItemView = holder.itemView
        holderItemView.word_text.text = getPosition.Word
        holderItemView.phonetic_text.text = getPosition.Phonetic
        holderItemView.open_desc_button.setOnClickListener {
            val showDesc = DescPopUp(getPosition)
            showDesc.show((activity).supportFragmentManager, "showDesc")
        }
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
        holderItemView.add_remove_learned_button.setOnClickListener {
            if(getPosition.IsLearned == 1){
                learnedWordsListViewModel.removeWordLearned(getPosition.Word)
                holderItemView.add_remove_learned_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_border_24)
                getPosition.IsLearned = 0
            }else{
                learnedWordsListViewModel.addRecordedWordLearned(getPosition.Word)
                holderItemView.add_remove_learned_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_24)
                getPosition.IsLearned = 1
            }
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
        holderItemView.add_remove_favorite_button.setOnClickListener{
            if(getPosition.IsFav == 1){
                learnedWordsListViewModel.removeWordFavorites(getPosition.Word)
                holderItemView.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_border_24)
                getPosition.IsFav = 0
            }else{
                learnedWordsListViewModel.addRecordedWordFavorites(getPosition.Word)
                holderItemView.add_remove_favorite_button.foreground = ContextCompat.getDrawable(context, R.drawable.ic_baseline_favorite_24)
                getPosition.IsFav = 1
            }
        }
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    fun updateWordList(newWordList : List<WordViewModel>){
        wordsList.clear()
        wordsList.addAll(newWordList)
        notifyDataSetChanged()
    }
}