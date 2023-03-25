package com.example.puppydictinary.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.puppydictinary.model.WordViewModel
import com.example.puppydictinary.model.YandexDef
import com.example.puppydictinary.model.entities.Word
import com.example.puppydictinary.model.entities.WordsLanguages
import com.example.puppydictinary.service.sqliteservice.CategoryService
import com.example.puppydictinary.service.sqliteservice.LanguageService
import com.example.puppydictinary.service.sqliteservice.WordService
import com.example.puppydictinary.service.sqliteservice.WordsLanguagesService

class LearnedWordListViewModel(activity: Activity, myLang: String, learningLang: String) : ViewModel() {
    private val _db = activity.openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
    private var languageService : LanguageService
    private var categoryService : CategoryService
    private var wordService : WordService
    private var wordsLanguagesService : WordsLanguagesService
    private var myLangId : Int = 0
    private var learningLangId : Int = 0

    val resultWords = MutableLiveData<List<WordViewModel>>()

    init {
        languageService = LanguageService(_db, myLang, myLang)
        myLangId = getLangIdByCode(myLang)
        learningLangId = getLangIdByCode(learningLang)
        categoryService = CategoryService(_db, myLangId, learningLangId)
        wordService = WordService(_db, myLangId, learningLangId)
        wordsLanguagesService = WordsLanguagesService(_db, myLangId, learningLangId)
    }

    fun getLangIdByCode(code: String): Int{
        return languageService.getIdByCode(code)
    }

    fun refreshData(){
        var wordViewModels: MutableList<WordViewModel> = mutableListOf()
        val learnedWords = getLearnedWords()
        for(item in learnedWords){
            val learnedWordsDescs = getDescriptionsByWordId(item.Id)
            var description = ""
            for(desc in learnedWordsDescs){
                description += "\n${getCategoryNameById(desc.CategoryId)}:\n${desc.Description}\n"
            }
            wordViewModels.add(WordViewModel(item.Id, item.LangId, item.DescLangId, item.Word, item.Phonetic, item.IsFav, item.IsLearned, description))
        }
        resultWords.value = wordViewModels
    }

    fun getLearnedWords(): List<Word>{
        return wordService.get().filter { it.IsLearned == 1 }
    }

    fun getDescriptionsByWordId(id: Int): List<WordsLanguages>{
        return wordsLanguagesService.get().filter{ it.WordId == id }
    }

    fun getCategoryNameById(id: Int): String? {
        return categoryService.getById(id)?.Name
    }

    fun isLearnedWord(word: String): Boolean{
        val id = wordService.getIdByName(word)
        if(id != 0){
            return wordService.getById(id)?.IsLearned == 1
        }
        return false
    }

    fun addWordLearned(yandexDef: List<YandexDef>){
        val wordId = wordService.getIdByName(yandexDef[0].text)
        if(wordId != 0){
            val word = wordService.getById(wordId)
            if (word != null) {
                word.IsLearned = 1
                wordService.update(word)
            }
        }else{
            var description = ""
            wordService.add(Word(0, learningLangId, myLangId, yandexDef[0].text, yandexDef[0].ts, 0, 1))
            val wordId = wordService.getIdByName(yandexDef[0].text)
            for(item in yandexDef){
                description = ""
                for(desc in item.tr){
                    description += if(desc == item.tr.lastOrNull()) desc.text else desc.text + ", "
                }
                wordsLanguagesService.add(WordsLanguages(wordId, categoryService.getIdByName(item.pos), description))
            }
        }
    }

    fun addRecordedWordLearned(wordText: String){
        val wordId = wordService.getIdByName(wordText)
        if(wordId != 0){
            val word = wordService.getById(wordId)
            if (word != null) {
                word.IsLearned = 1
                wordService.update(word)
            }
        }
    }

    fun addRecordedWordFavorites(wordText: String){
        val wordId = wordService.getIdByName(wordText)
        if(wordId != 0){
            val word = wordService.getById(wordId)
            if (word != null) {
                word.IsFav = 1
                wordService.update(word)
            }
        }
    }

    fun removeWordLearned(wordText: String){
        val word = wordService.getById(wordService.getIdByName(wordText))
        if (word != null) {
            word.IsLearned = 0
            wordService.update(word)
        }
    }

    fun removeWordFavorites(wordText: String){
        val word = wordService.getById(wordService.getIdByName(wordText))
        if (word != null) {
            word.IsFav = 0
            wordService.update(word)
        }
    }
}