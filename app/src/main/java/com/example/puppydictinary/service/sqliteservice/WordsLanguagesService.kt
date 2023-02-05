package com.example.puppydictinary.service.sqliteservice

import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.entities.WordsLanguages

class WordsLanguagesService (db: SQLiteDatabase, myLangId: Int, learningLangId: Int) : SQLiteService<WordsLanguages>{
    private val _db = db
    private val _myLangId = myLangId
    private val _learningLangId = learningLangId

    override fun getIdByName(name: String): Int {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): WordsLanguages {
        TODO("Not yet implemented")
    }

    override fun get(): List<WordsLanguages> {
        TODO("Not yet implemented")
    }

    override fun create(entity: WordsLanguages) {
        TODO("Not yet implemented")
    }

    override fun update(entity: WordsLanguages) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun createTable(){
        _db.execSQL("CREATE TABLE IF NOT EXISTS WordsLanguages (WordId INTEGER NOT NULL, DescLangId INTEGER NOT NULL, CategoryId INTEGER NOT NULL, Description NVARCHAR(200) NOT NULL, IsFav BOOLEAN DEFAULT 0 NOT NULL, IsLearned BOOLEAN DEFAULT 0 NOT NULL, FOREIGN KEY (WordId) REFERENCES Words (Id), FOREIGN KEY (DescLangId) REFERENCES Languages(Id), FOREIGN KEY(CategoryId) REFERENCES Categories(Id))")
    }

    override fun insertTable(){
        //db.execSQL("INSERT INTO WordsLanguages (WordId, DescLangId, Description) VALUES (1, 2, 'about, on, respecting, regarding, round'), (1, 3, 'über'), (1, 2, 'all, completely, unequivocally'), (1, 3, 'Gesamt, all')")
    }
}