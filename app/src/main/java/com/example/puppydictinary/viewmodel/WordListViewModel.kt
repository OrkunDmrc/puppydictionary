package com.example.puppydictinary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.puppydictinary.model.Yandex
import com.example.puppydictinary.model.YandexDef
import com.example.puppydictinary.service.YandexAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main_menu.*

class WordListViewModel : ViewModel() {
    val resultWords = MutableLiveData<List<YandexDef>>()
    val resultErrorMessage = MutableLiveData<Boolean>()
    val progressCircular = MutableLiveData<Boolean>()
    val wordInformation = MutableLiveData<Boolean>()

    private val service = YandexAPIService()
    private val disposable = CompositeDisposable()

    fun firstData(){
        resultErrorMessage.value = false
        progressCircular.value = false
        wordInformation.value = false
    }

    fun refreshData(langFrom: String, langTo: String, text: String, ui: String){
        getMeansFromYandex(langFrom, langTo, text, ui)
    }

    private fun getMeansFromYandex(langFrom: String, langTo: String, text: String, ui: String){
        progressCircular.value = true
        disposable.add(
            service.getData("$langFrom-$langTo", text, ui)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Yandex>(){
                    override fun onSuccess(t: Yandex){
                        if(t.def.isNotEmpty()) {
                            resultWords.value = t.def
                            wordInformation.value = true
                            resultErrorMessage.value = false
                        }else{
                            resultErrorMessage.value = true
                            wordInformation.value = false
                        }
                        progressCircular.value = false
                    }
                    override fun onError(e: Throwable){
                        resultErrorMessage.value = true
                        progressCircular.value = false
                        wordInformation.value = false
                        e.printStackTrace()
                    }
                }
                )
        )
    }
}