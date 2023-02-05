package com.example.puppydictinary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.puppydictinary.service.SQLiteService

class FavoriteWordsListViewModelFactory(private var SQLiteService : SQLiteService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteWordsListViewModel::class.java)){
            return FavoriteWordsListViewModel(SQLiteService) as T
        }
        throw java.lang.IllegalArgumentException("FavoriteWordsListViewModel class not found")
        return super.create(modelClass)
    }
}