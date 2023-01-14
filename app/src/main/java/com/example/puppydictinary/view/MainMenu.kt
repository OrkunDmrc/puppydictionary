package com.example.puppydictinary.view

import android.content.Context
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.puppydictinary.R
import com.example.puppydictinary.model.Yandex
import com.example.puppydictinary.service.YandexAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainMenu : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var sharedReferences = requireActivity().getSharedPreferences("com.example.puppydictinary.view", Context.MODE_PRIVATE)
        if(sharedReferences.getBoolean("isItFirst", true)){
            sharedReferences.edit().putBoolean("isItFirst",false).apply()
            val db = requireActivity().openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
            db.execSQL("CREATE TABLE IF NOT EXISTS Languages (Id INTEGER PRIMARY KEY, Name NVARCHAR(50) NOT NULL)")
            db.execSQL("CREATE TABLE IF NOT EXISTS Categories (Id INTEGER PRIMARY KEY, Name NVARCHAR(50) NOT NULL)")
            db.execSQL("CREATE TABLE IF NOT EXISTS Words (Id INTEGER PRIMARY KEY, LangId INTEGER NOT NULL, CategoryId INTEGER NOT NULL, Word NVARCHAR(50) NOT NULL, Is100 BOOLEAN DEFAULT 0 NOT NULL, Is1000 BOOLEAN DEFAULT 0 NOT NULL, FOREIGN KEY(LangId) REFERENCES Languages(Id), FOREIGN KEY(CategoryId) REFERENCES Categories(Id))")
            db.execSQL("CREATE TABLE IF NOT EXISTS WordsLanguages (WordId INTEGER NOT NULL, DescLangId INTEGER NOT NULL, Description NVARCHAR(200) NOT NULL, IsFav BOOLEAN DEFAULT 0 NOT NULL, IsLearned BOOLEAN DEFAULT 0 NOT NULL, FOREIGN KEY (WordId) REFERENCES Words (Id), FOREIGN KEY (DescLangId) REFERENCES Languages(Id))")

            db.execSQL("INSERT INTO Languages (Name) VALUES ('Türkçe'), ('İngilizce'), ('Almanca')")
            db.execSQL("INSERT INTO Categories (Name) VALUES ('Diğer'), ('Fiil'), ('İsim'), ('Sıfat'), ('Zamir'), ('Zarf'), ('Edat'), ('Bağlaç')")
            db.execSQL("INSERT INTO Words (LangId, CategoryId, Word) VALUES (1, 7, 'hakkında'),(1, 6, 'tüm')")
            db.execSQL("INSERT INTO WordsLanguages (WordId, DescLangId, Description) VALUES (1, 2, 'about, on, respecting, regarding, round'), (1, 3, 'über'), (1, 2, 'all, completely, unequivocally'), (1, 3, 'Gesamt, all')")

            var cursor = db.rawQuery("SELECT * FROM Languages", null)
            var id = cursor.getColumnIndex("Id")
            var name = cursor.getColumnIndex("Name")
            while (cursor.moveToNext()){
                println("-------- Languages -> ID : ${cursor.getInt(id)} Name : ${cursor.getString(name)}")
            }
            cursor = db.rawQuery("SELECT * FROM Categories", null)
            id = cursor.getColumnIndex("Id")
            name = cursor.getColumnIndex("Name")
            while (cursor.moveToNext()){
                println("-------- Categories -> ID : ${cursor.getInt(id)} Name : ${cursor.getString(name)}")
            }
            cursor = db.rawQuery("SELECT * FROM Words", null)
            id = cursor.getColumnIndex("Id")
            var langId = cursor.getColumnIndex("LangId")
            var catId = cursor.getColumnIndex("CategoryId")
            var word = cursor.getColumnIndex("Word")
            while (cursor.moveToNext()){
                println("-------- Words -> ID : ${cursor.getInt(id)} LangId : ${cursor.getInt(langId)} catId : ${cursor.getInt(catId)} word : ${cursor.getString(word)}")
            }
            cursor = db.rawQuery("SELECT * FROM WordsLanguages", null)
            id = cursor.getColumnIndex("WordId")
            langId = cursor.getColumnIndex("DescLangId")
            var desc = cursor.getColumnIndex("Description")
            var isFav = cursor.getColumnIndex("IsFav")
            var isLearned = cursor.getColumnIndex("IsLearned")
            while (cursor.moveToNext()){
                println("-------- WordsLanguages -> WordID : ${cursor.getInt(id)} DescLangId : ${cursor.getInt(langId)} desc : ${cursor.getString(desc)} IsFav : ${cursor.getString(isFav)} IsLearned : ${cursor.getString(isLearned)}")
            }
            cursor.close()
            view?.let { Navigation.findNavController(it).navigate(MainMenuDirections.actionMainMenuToFlags()) }
        }else{
            val db = requireActivity().openOrCreateDatabase("DictinaryDB", Context.MODE_PRIVATE,null)
            val cursor = db.rawQuery("SELECT w.Id, w.Word, wl.Description FROM WordsLanguages as wl join Words as w on w.Id = wl.WordId",null)
            val id = cursor.getColumnIndex("Id")
            val word = cursor.getColumnIndex("Word")
            val desc = cursor.getColumnIndex("Description")
            while (cursor.moveToNext()){
                println("-------- WordsLanguages Words -> WordID : ${cursor.getInt(id)} word : ${cursor.getString(word)} desc : ${cursor.getString(desc)}")
            }
            cursor.close()

            val service = YandexAPIService()
            val disposable = CompositeDisposable()
            disposable.add(
                service.getData("tr-en", "araba")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object: DisposableSingleObserver<Yandex>(){
                        override fun onSuccess(t: Yandex){
                            if(t != null) {
                                println("eleman sayısı:" + t.def.count())
                                println(t.def[0].pos + " " + t.def[0].text)
                                println(t.def[0].tr[0].text)
                            }
                        }
                        override fun onError(e: Throwable){

                        }
                    }
                    )
            )
        }
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

}