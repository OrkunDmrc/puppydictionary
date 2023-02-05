package com.example.puppydictinary.service.sqliteservice

import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.entities.Word

class WordService(db: SQLiteDatabase, myLangId: Int, learningLangId: Int) : SQLiteService<Word> {
    private val _db = db
    private val _myLangId = myLangId
    private val _learningLangId = learningLangId

    override fun getIdByName(name: String): Int {
        val cursor = _db.rawQuery("SELECT w.Id FROM Words as w JOIN WordsLanguages as wl on w.Id = wl.WordId WHERE w.Word = '${name}' and w.LangId = $_myLangId and wl.DescLangId = $_learningLangId",null)
        val idIndex = cursor.getColumnIndex("Id")
        var id = 0
        if(cursor.moveToFirst()) {
            id = cursor.getInt(idIndex)
        }
        cursor.close()
        return id
    }

    override fun getById(id: Int): Word {
        TODO("Not yet implemented")
    }

    override fun get(): List<Word> {
        TODO("Not yet implemented")
    }

    override fun create(entity: Word) {
        TODO("Not yet implemented")
    }

    override fun update(entity: Word) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun createTable(){
        _db.execSQL("CREATE TABLE IF NOT EXISTS Words (Id INTEGER PRIMARY KEY, LangId INTEGER NOT NULL, Word NVARCHAR(50) NOT NULL, Phonetic NVARCHAR(50) NOT NULL, Is100 BOOLEAN DEFAULT 0 NOT NULL, Is1000 BOOLEAN DEFAULT 0 NOT NULL, FOREIGN KEY(LangId) REFERENCES Languages(Id))")
    }

    override fun insertTable(){
        //db.execSQL("INSERT INTO Words (LangId, Word) VALUES (2, 'about'), (2, 'tüm')")
    }

}