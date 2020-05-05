package com.byfrunze.memshelper.presenters

import com.byfrunze.memshelper.data.ModelSaveQuotes
import com.byfrunze.memshelper.providers.ProviderHistory
import com.byfrunze.memshelper.views.ViewHistory
import io.realm.RealmResults
import moxy.MvpPresenter

class PresenterHistory : MvpPresenter<ViewHistory>() {
    fun loadHistory() {
        viewState.load()
        ProviderHistory(this).loadQuotes()
    }

    fun setupList(list: RealmResults<ModelSaveQuotes>) {
        viewState.completeLoading(list)
    }

    fun loadEmptyList() {
        viewState.showEmptyHistory()
    }

    fun error(localizedMessage: String?) {
        viewState.errorLoading(localizedMessage)
    }

    fun searchInfo(query: String) {
        ProviderHistory(this).searchQuotes(query = query)
    }
}