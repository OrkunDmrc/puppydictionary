package com.example.puppydictinary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.puppydictinary.model.Word
import com.example.puppydictinary.service.SQLiteService

class FavoriteWordsListViewModel(private var SQLiteService : SQLiteService) : ViewModel() {
    val resultWords = MutableLiveData<List<Word>>()

    fun setFirstAdjust(){
        SQLiteService.createTables()
        SQLiteService.insertTables()
    }

    fun getFavoriteWordsList(): List<Word>{
        return SQLiteService.getFavoriteWordsList()
    }

    fun isFavoriteWord(word: String): Boolean{
        return SQLiteService.isFavoriteWord(word)
    }

    fun addWordFavorites(words: List<Word>){
        if(SQLiteService.isRecordedWord(words[0].Word)){
            SQLiteService.updateWordAsFavorite(words[0].Word)
        }else{
            SQLiteService.addWordFavorites(words)
        }
    }

    fun removeWordFavorites(word: String){
        SQLiteService.removeWordFavorites(SQLiteService.findWordIdByWord(word))
    }

    fun findCategoryIdByName(name: String): Int{
        return SQLiteService.findCategoryIdByName(name)
    }

}