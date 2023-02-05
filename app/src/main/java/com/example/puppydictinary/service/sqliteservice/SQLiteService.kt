package com.example.puppydictinary.service.sqliteservice

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.entities.Word

interface SQLiteService<T>/*(/*activity: Activity, myLang: String, learningLang: String*/)*/ {

    fun getIdByName(name: String): Int

    fun getById(id: Int): T?

    fun get() : List<T>

    fun add(entity: T)

    fun update(entity: T)

    fun delete(id: Int)

    fun createTable()

    fun insertTable()
    /*val db = activity.openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
    val myLangId = findLangIdByCode(myLang)
    val learningLangId = findLangIdByCode(learningLang)*/

    /*fun createTables(){



    }

    fun insertTables(){



    }*/

    /*fun findLangIdByCode(langCode: String): Int{
        if(langCode.isNotEmpty()){
            val cursor = db.rawQuery("SELECT Id From Languages WHERE Code = '${langCode}'", null)
            val idIndex = cursor.getColumnIndex("Id")
            var id = 0
            if(cursor.moveToFirst()) {
                id = cursor.getInt(idIndex)
            }
            cursor.close()
            return id
        }
        return 0
    }



    fun getFavoriteWordsList(): List<Word> {
        val wordsList = arrayListOf<Word>()
        val cursor = db.rawQuery("SELECT w.Id, c.Id as CatId, c.Name as CatName, w.Word, w.Phonetic, wl.Description FROM WordsLanguages as wl JOIN Words as w on w.Id = wl.WordId JOIN Categories as c on c.Id = w.CategoryId WHERE w.LangId = ${learningLangId} and wl.DescLangId = ${myLangId} and wl.IsFav = 1",null)
        val idIndex = cursor.getColumnIndex("Id")
        val catIdIndex = cursor.getColumnIndex("CatId")
        val catNameIndex = cursor.getColumnIndex("CatName")
        val wordIndex = cursor.getColumnIndex("Word")
        val phoneticIndex = cursor.getColumnIndex("Phonetic")
        val descIndex = cursor.getColumnIndex("Description")
        while(cursor.moveToNext()){
            wordsList.add(Word(cursor.getInt(idIndex), cursor.getInt(catIdIndex), cursor.getString(catNameIndex), cursor.getString(wordIndex),cursor.getString(phoneticIndex), cursor.getString(descIndex)))
        }
        return wordsList
    }

    fun isFavoriteWord(word: String) : Boolean{
        val cursor = db.rawQuery("SELECT w.Id FROM Words as w join WordsLanguages as wl on w.Id = wl.WordId GROUP BY w.Id HAVING w.Word = '${word}' and w.LangId = ${myLangId} and wl.DescLangId = ${learningLangId} and wl.IsFav = 1",null)
        val idIndex = cursor.getColumnIndex("Id")
        var isFav = false
        if(cursor.moveToFirst()) {
            isFav = cursor.getInt(idIndex) != 0
        }
        cursor.close()
        return isFav
    }

    fun addWordFavorites(words: List<Word>){
        var cursor = db.rawQuery("INSERT INTO Words (LangId, Word, Phonetic) VALUES (${learningLangId}, '${words[0].Word}', '${words[0].Phonetic}')",null)
        val wordId = findWordsTableLastId()
        for (word in words){
            cursor = db.rawQuery("INSERT INTO WordsLanguages (WordId, DescLangId, Description, IsFav) VALUES (${wordId}, ${myLangId}, ${word.Description}, 1))",null)
        }
        cursor.close()
    }

    fun findTableLastId(tableName: String){

    }

    fun updateWordAsFavorite(word: String){
        val wordId = findWordIdByWord(word)
        val cursor = db.rawQuery("UPDATE WordsLanguages SET IsFav = 1 WHERE WordId = ${wordId}",null)
        cursor.close()
    }

    fun removeWordFavorites(WordId: Int){
        val cursor = db.rawQuery("UPDATE WorkLanguages SET (IsFav = 0) WHERE WordId = $WordId",null)
        cursor.close()
    }*/




}