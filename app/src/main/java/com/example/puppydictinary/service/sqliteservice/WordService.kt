package com.example.puppydictinary.service.sqliteservice

import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.entities.Word

class WordService(db: SQLiteDatabase, myLangId: Int, learningLangId: Int) : SQLiteService<Word> {
    private val _db = db
    private val _myLangId = myLangId
    private val _learningLangId = learningLangId

    override fun getIdByName(name: String): Int {
        val cursor = _db.rawQuery("SELECT Id FROM Words WHERE Word = '${name}' and LangId = ${_learningLangId} and DescLangId = ${_myLangId}",null)
        val idIndex = cursor.getColumnIndex("Id")
        var id = 0
        if(cursor.moveToFirst()) {
            id = cursor.getInt(idIndex)
        }
        cursor.close()
        return id
    }

    override fun getById(id: Int): Word? {
        var word: Word? = null
        val cursor = _db.rawQuery("SELECT Id, LangId, DescLangId, Word, Phonetic, IsFav, IsLearned  FROM Words WHERE Id = $id and LangId = ${_learningLangId} and DescLangId = ${_myLangId}",null)
        if(cursor.count != 0){
            val idIndex = cursor.getColumnIndex("Id")
            val langIdIndex = cursor.getColumnIndex("LangId")
            val descIdIndex = cursor.getColumnIndex("DescLangId")
            val wordIndex = cursor.getColumnIndex("Word")
            val phoneticIndex = cursor.getColumnIndex("Phonetic")
            val isFavIndex = cursor.getColumnIndex("IsFav")
            val isLearnedIndex = cursor.getColumnIndex("IsLearned")
            while(cursor.moveToNext()) {
                word = Word(cursor.getInt(idIndex), cursor.getInt(langIdIndex), cursor.getInt(descIdIndex), cursor.getString(wordIndex), cursor.getString(phoneticIndex), cursor.getInt(isFavIndex), cursor.getInt(isLearnedIndex))
            }
            cursor.close()
        }
        return word
    }

    override fun get(): List<Word> {
        var words: MutableList<Word> = mutableListOf()
        val cursor = _db.rawQuery("SELECT Id, LangId, DescLangId, Word, Phonetic, IsFav, IsLearned FROM Words WHERE LangId = ${_learningLangId} and DescLangId = ${_myLangId}",null)
        if(cursor.count != 0){
            val idIndex = cursor.getColumnIndex("Id")
            val langIdIndex = cursor.getColumnIndex("LangId")
            val descIdIndex = cursor.getColumnIndex("DescLangId")
            val wordIndex = cursor.getColumnIndex("Word")
            val phoneticIndex = cursor.getColumnIndex("Phonetic")
            val isFavIndex = cursor.getColumnIndex("IsFav")
            val isLearnedIndex = cursor.getColumnIndex("IsLearned")
            while(cursor.moveToNext()) {
                words.add(Word(cursor.getInt(idIndex), cursor.getInt(langIdIndex), cursor.getInt(descIdIndex), cursor.getString(wordIndex), cursor.getString(phoneticIndex), cursor.getInt(isFavIndex), cursor.getInt(isLearnedIndex)))
            }
            cursor.close()
        }
        return words
    }

    override fun add(word: Word) {
        _db.execSQL("INSERT INTO Words (LangId, DescLangId, Word, Phonetic, IsFav, IsLearned) VALUES (${word.LangId}, ${word.DescLangId}, '${word.Word}', '${word.Phonetic}', ${word.IsFav}, ${word.IsLearned})")
    }

    override fun update(word: Word) {
        _db.execSQL("UPDATE Words SET IsFav = ${word.IsFav}, IsLearned = ${word.IsLearned} WHERE Id = ${word.Id}")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun createTable(){
        _db.execSQL("CREATE TABLE IF NOT EXISTS Words (Id INTEGER PRIMARY KEY, LangId INTEGER NOT NULL, DescLangId INTEGER NOT NULL, Word NVARCHAR(50) NOT NULL, Phonetic NVARCHAR(50), IsFav BOOLEAN DEFAULT 0 NOT NULL, IsLearned BOOLEAN DEFAULT 0 NOT NULL, FOREIGN KEY(LangId) REFERENCES Languages(Id), FOREIGN KEY(DescLangId) REFERENCES Languages(Id))")
    }

    override fun insertTable(){
        //db.execSQL("INSERT INTO Words (LangId, Word) VALUES (2, 'about'), (2, 'tüm')")
    }

}