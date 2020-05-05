package com.byfrunze.memshelper.providers

import android.util.Log
import com.byfrunze.memshelper.data.ModelSaveQuotes
import com.byfrunze.memshelper.data.RealmDB
import com.byfrunze.memshelper.presenters.PresenterHistory
import io.realm.Case
import io.realm.kotlin.where
import java.lang.Exception

class ProviderHistory(val presenter: PresenterHistory) {

    val realm = RealmDB.initDB()
    fun loadQuotes() {
        val quotes = realm.where<ModelSaveQuotes>().findAll()
        try{
            if (quotes.count() != 0) presenter.setupList(quotes)
            else presenter.loadEmptyList()
        }catch (e : Exception){
            presenter.error(e.localizedMessage)
        }finally {
            realm.close()
        }
    }

    fun searchQuotes(query: String) {
        val quotes = realm.where<ModelSaveQuotes>().contains("from", query)
            .or().contains("quoteRu", query, Case.INSENSITIVE)
            .or().contains("quoteEn", query, Case.INSENSITIVE)
            .or().contains("author", query, Case.INSENSITIVE).findAllAsync()
        presenter.setupList(quotes)
    }
}