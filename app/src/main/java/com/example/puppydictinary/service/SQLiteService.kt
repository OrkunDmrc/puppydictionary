package com.example.puppydictinary.service

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.puppydictinary.model.Word

class SQLiteService(activity: Activity, myLang: String, learningLang: String) {

    val db = activity.openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
    val myLangId = findLangIdByCode(myLang)
    val learningLangId = findLangIdByCode(learningLang)

    fun createTables(){
        db.execSQL("CREATE TABLE IF NOT EXISTS Languages (Id INTEGER PRIMARY KEY, Name NVARCHAR(50), Code NVARCHAR(10) NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS Categories (Id INTEGER PRIMARY KEY, LangId INTEGER NOT NULL, Name NVARCHAR(50) NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS Words (Id INTEGER PRIMARY KEY, LangId INTEGER NOT NULL, Word NVARCHAR(50) NOT NULL, Phonetic NVARCHAR(50) NOT NULL, Is100 BOOLEAN DEFAULT 0 NOT NULL, Is1000 BOOLEAN DEFAULT 0 NOT NULL, FOREIGN KEY(LangId) REFERENCES Languages(Id))")
        db.execSQL("CREATE TABLE IF NOT EXISTS WordsLanguages (WordId INTEGER NOT NULL, DescLangId INTEGER NOT NULL, CategoryId INTEGER NOT NULL, Description NVARCHAR(200) NOT NULL, IsFav BOOLEAN DEFAULT 0 NOT NULL, IsLearned BOOLEAN DEFAULT 0 NOT NULL, FOREIGN KEY (WordId) REFERENCES Words (Id), FOREIGN KEY (DescLangId) REFERENCES Languages(Id), FOREIGN KEY(CategoryId) REFERENCES Categories(Id))")
    }

    fun insertTables(){
        db.execSQL("INSERT INTO Languages (Name, Code) VALUES ('Türkçe', 'tr'), ('İngilizce', 'en'), ('Almanca','de')")
        db.execSQL("INSERT INTO Categories (LangId, Name) VALUES (1, 'diğer'), (1, 'fiil'), (1, 'isim'), (1, 'sıfat'), (1, 'zamir'), (1, 'zarf'), (1, 'edat'), (1, 'bağlaç')")
        //db.execSQL("INSERT INTO Words (LangId, Word) VALUES (2, 'about'), (2, 'tüm')")
        //db.execSQL("INSERT INTO WordsLanguages (WordId, DescLangId, Description) VALUES (1, 2, 'about, on, respecting, regarding, round'), (1, 3, 'über'), (1, 2, 'all, completely, unequivocally'), (1, 3, 'Gesamt, all')")
    }

    fun findLangIdByCode(langCode: String): Int{
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

    fun isRecordedWord(word: String): Boolean{
        val cursor = db.rawQuery("SELECT w.Id FROM Words as w join WordsLanguages as wl on w.Id = wl.WordId GROUP BY w.Id HAVING w.Word = '${word}' and w.LangId = ${myLangId} and wl.DescLangId = ${learningLangId}",null)
        val idIndex = cursor.getColumnIndex("Id")
        var isRecorded = false
        if(cursor.moveToFirst()) {
            isRecorded = cursor.getInt(idIndex) != 0
        }
        cursor.close()
        return isRecorded
    }

    fun findWordIdByWord(word: String): Int{
        val cursor = db.rawQuery("SELECT w.Id FROM Words as w JOIN WordsLanguages as wl on w.Id = wl.WordId WHERE w.Word = '${word}' and w.LangId = $myLangId and wl.DescLangId = $learningLangId",null)
        val idIndex = cursor.getColumnIndex("Id")
        var id = 0
        if(cursor.moveToFirst()) {
            id = cursor.getInt(idIndex)
        }
        cursor.close()
        return id
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
    }

    fun findCategoryIdByName(name: String): Int{
        val cursor = db.rawQuery("SELECT Id FROM Categories WHERE Name = '${name}'", null)
        val idIndex = cursor.getColumnIndex("Id")
        var id = 0
        if(cursor.moveToFirst()) {
            id = cursor.getInt(idIndex)
        }
        return id
    }


}