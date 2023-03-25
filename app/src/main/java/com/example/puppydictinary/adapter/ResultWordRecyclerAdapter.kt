package com.example.puppydictinary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puppydictinary.R
import com.example.puppydictinary.model.YandexDef
import kotlinx.android.synthetic.main.result_word_recycler_row.view.*

class ResultWordRecyclerAdapter(var wordList : ArrayList<YandexDef>) : RecyclerView.Adapter<ResultWordRecyclerAdapter.ResultWordVH>() {
    class ResultWordVH(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultWordVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.result_word_recycler_row, parent,false)
        return ResultWordVH(view)
    }

    override fun onBindViewHolder(holder: ResultWordVH, position: Int) {
        val getPosition = wordList.get(position)
        val holderItemView = holder.itemView
        var meanings = ""
        holderItemView.response_text_from_lang.text = getPosition.text
        holderItemView.type_text.text = "${getPosition.pos}"
        holderItemView.phonetic_text.text = "(${getPosition.ts})"
        for(tr in getPosition.tr){
            meanings += if(tr == getPosition.tr.lastOrNull()) tr.text else tr.text + ", "
        }
        holderItemView.response_text_to_lang.text = meanings
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    fun updateWordList(newWordList : List<YandexDef>){
        wordList.clear()
        wordList.addAll(newWordList)
        notifyDataSetChanged()
    }
}