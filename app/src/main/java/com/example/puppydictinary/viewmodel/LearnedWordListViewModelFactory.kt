package com.example.puppydictinary.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LearnedWordListViewModelFactory(private val activity: Activity, private val myLang: String, private val learningLang: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LearnedWordListViewModel::class.java)){
            return LearnedWordListViewModel(activity, myLang, learningLang) as T
        }
        throw java.lang.IllegalArgumentException("FavoriteWordsListViewModel class not found")
        return super.create(modelClass)
    }
}