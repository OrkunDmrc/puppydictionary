package com.example.puppydictinary.service.sqliteservice

import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.entities.Word
import com.example.puppydictinary.model.entities.WordsLanguages

class WordsLanguagesService (db: SQLiteDatabase, myLangId: Int, learningLangId: Int) : SQLiteService<WordsLanguages>{
    private val _db = db
    private val _myLangId = myLangId
    private val _learningLangId = learningLangId

    override fun getIdByName(name: String): Int {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): WordsLanguages? {
        TODO("Not yet implemented")
    }

    fun getAllById(id: Int): List<WordsLanguages>? {
        var wordsLanguages: MutableList<WordsLanguages> = mutableListOf()
        val cursor = _db.rawQuery("SELECT WordId, CategoryId, Description FROM WordsLanguages WHERE WordId = ${id}",null)
        if(cursor.count != 0){
            val wordIdIndex = cursor.getColumnIndex("WordId")
            val categoryIdIndex = cursor.getColumnIndex("CategoryId")
            val descriptionIndex = cursor.getColumnIndex("Description")
            while(cursor.moveToNext()) {
                wordsLanguages.add(WordsLanguages(cursor.getInt(wordIdIndex), cursor.getInt(categoryIdIndex), cursor.getString(descriptionIndex)))
            }
            cursor.close()
        }
        return wordsLanguages
    }

    override fun get(): List<WordsLanguages> {
        var wordsLanguages: MutableList<WordsLanguages> = mutableListOf()
        val cursor = _db.rawQuery("SELECT WordId, CategoryId, Description FROM WordsLanguages",null)
        if(cursor.count != 0){
            val wordIdIndex = cursor.getColumnIndex("WordId")
            val categoryIdIndex = cursor.getColumnIndex("CategoryId")
            val descriptionIndex = cursor.getColumnIndex("Description")
            while(cursor.moveToNext()) {
                wordsLanguages.add(WordsLanguages(cursor.getInt(wordIdIndex), cursor.getInt(categoryIdIndex), cursor.getString(descriptionIndex)))
            }
            cursor.close()
        }
        return wordsLanguages
    }

    override fun add(wordsLanguages: WordsLanguages) {
        _db.execSQL("INSERT INTO WordsLanguages (WordId, CategoryId, Description) VALUES (${wordsLanguages.WordId}, ${wordsLanguages.CategoryId}, '${wordsLanguages.Description}')")
    }

    override fun update(entity: WordsLanguages) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun createTable(){
        _db.execSQL("CREATE TABLE IF NOT EXISTS WordsLanguages (WordId INTEGER NOT NULL, CategoryId INTEGER NOT NULL, Description NVARCHAR(300) NOT NULL, FOREIGN KEY (WordId) REFERENCES Words (Id), FOREIGN KEY(CategoryId) REFERENCES Categories(Id))")

    }

    override fun insertTable(){
        //db.execSQL("INSERT INTO WordsLanguages (WordId, DescLangId, Description) VALUES (1, 2, 'about, on, respecting, regarding, round'), (1, 3, 'über'), (1, 2, 'all, completely, unequivocally'), (1, 3, 'Gesamt, all')")
    }
}