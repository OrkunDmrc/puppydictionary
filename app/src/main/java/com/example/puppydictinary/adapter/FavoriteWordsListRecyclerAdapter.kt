package com.example.puppydictinary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.entities.Word
import kotlinx.android.synthetic.main.favorite_words_list_recycler_row.view.*
import kotlinx.android.synthetic.main.favorite_words_list_recycler_row.view.phonetic_text
import kotlinx.android.synthetic.main.favorite_words_list_recycler_row.view.response_text_from_lang
import kotlinx.android.synthetic.main.favorite_words_list_recycler_row.view.type_text

class FavoriteWordsListRecyclerAdapter(var wordsList: ArrayList<Word>) : RecyclerView.Adapter<FavoriteWordsListRecyclerAdapter.FavoriteWordsListVH>(){
    class FavoriteWordsListVH(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteWordsListVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.favorite_words_list_recycler_row, parent,false)
        return FavoriteWordsListVH(view)
    }

    override fun onBindViewHolder(holder: FavoriteWordsListVH, position: Int) {
        val getPosition = wordsList.get(position)
        val holderItemView = holder.itemView
        holderItemView.response_text_from_lang.text = getPosition.Word
        holderItemView.type_text.text = getPosition.CategoryName
        holderItemView.phonetic_text.text = getPosition.Phonetic
        holderItemView.response_text_to_lang.text = getPosition.Description
        holderItemView.pronunciation_button.setOnClickListener {
            
        }
    }

    override fun getItemCount(): Int {
        return wordsList.size
    }

    fun updateWordList(newWordList : List<Word>){
        wordsList.clear()
        wordsList.addAll(newWordList)
        notifyDataSetChanged()
    }
}