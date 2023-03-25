package com.example.puppydictinary.service.sqliteservice

import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.entities.Language

class LanguageService(db: SQLiteDatabase, myLang: String, learningLang: String) : SQLiteService<Language>  {
    private val _db = db
    private val _myLangId = getIdByCode(myLang)
    private val _learningLangId = getIdByCode(learningLang)

    override fun getIdByName(name: String): Int {
        TODO("Not yet implemented")
    }

    fun getIdByCode(code: String): Int{
        if(code.isNotEmpty()){
            val cursor = _db.rawQuery("SELECT Id From Languages WHERE Code = '${code}'", null)
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

    override fun getById(id: Int): Language? {
        TODO("Not yet implemented")
    }

    override fun get(): List<Language> {
        TODO("Not yet implemented")
    }

    override fun add(entity: Language) {
        _db.execSQL("INSERT INTO Languages (Name, Code) VALUES (${entity.Name}, ${entity.Code})")
    }

    override fun update(entity: Language) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun createTable(){
        _db.execSQL("CREATE TABLE IF NOT EXISTS Languages (Id INTEGER PRIMARY KEY, Name NVARCHAR(50), Code NVARCHAR(10) NOT NULL)")
    }

    override fun insertTable(){
        _db.execSQL("INSERT INTO Languages (Name, Code) VALUES ('Türkçe', 'tr'), ('İngilizce', 'en'), ('Almanca','de')")
    }
}