package com.example.puppydictinary.service.sqliteservice

import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.entities.Category
import com.example.puppydictinary.model.entities.Word

class CategoryService(db: SQLiteDatabase, myLangId: Int, learningLangId: Int) : SQLiteService<Category> {
    private val _db = db
    private val _myLangId = myLangId
    private val _learningLangId = learningLangId

    override fun getIdByName(name: String): Int {
        val cursor = _db.rawQuery("SELECT Id FROM Categories WHERE Name = '${name}'", null)
        val idIndex = cursor.getColumnIndex("Id")
        var id = 0
        if(cursor.moveToFirst()) {
            id = cursor.getInt(idIndex)
        }
        return id
    }

    fun haveLangId(id: Int): Boolean {
        val cursor = _db.rawQuery("SELECT Id FROM Categories WHERE LangId = '${id}'", null)
        if(cursor.moveToFirst()) {
            return true
        }
        return false
    }

    override fun getById(id: Int): Category? {
        val cursor = _db.rawQuery("SELECT Id, LangId, Name FROM Categories WHERE Id = ${id}", null)
        val idIndex = cursor.getColumnIndex("Id")
        val langIdIndex = cursor.getColumnIndex("LangId")
        val nameIndex = cursor.getColumnIndex("Name")
        var category: Category? = null
        if(cursor.moveToFirst()) {
            category = Category(cursor.getInt(idIndex), cursor.getInt(langIdIndex), cursor.getString(nameIndex))
        }
        return category
    }

    override fun get(): List<Category> {
        var categories: MutableList<Category> = mutableListOf()
        val cursor = _db.rawQuery("SELECT Id, LangId, Name FROM Categories",null)
        if(cursor.count != 0){
            val idIndex = cursor.getColumnIndex("Id")
            val langIdIndex = cursor.getColumnIndex("LangId")
            val nameIndex = cursor.getColumnIndex("Name")
            while(cursor.moveToNext()) {
                categories.add(Category(cursor.getInt(idIndex), cursor.getInt(langIdIndex), cursor.getString(nameIndex)))
            }
            cursor.close()
        }
        return categories
    }

    override fun add(entity: Category) {
        _db.execSQL("INSERT INTO Categories (LangId, Name) VALUES (${entity.LangId}, '${entity.Name}')")
    }

    override fun update(entity: Category) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun createTable(){
        _db.execSQL("CREATE TABLE IF NOT EXISTS Categories (Id INTEGER PRIMARY KEY, LangId INTEGER NOT NULL, Name NVARCHAR(50) NOT NULL)")
    }

    override fun insertTable(){
        _db.execSQL("INSERT INTO Categories (LangId, Name) VALUES (1, 'diğer'), (1, 'fiil'), (1, 'isim'), (1, 'sıfat'), (1, 'zamir'), (1, 'zarf'), (1, 'edat'), (1, 'bağlaç')")
    }

}