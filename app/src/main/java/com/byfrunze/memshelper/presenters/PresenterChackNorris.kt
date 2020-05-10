package com.byfrunze.memshelper.presenters

import com.byfrunze.memshelper.providers.ProviderChackNorris
import com.byfrunze.memshelper.providers.ProviderMotivation
import com.byfrunze.memshelper.views.ViewChackNorris
import moxy.MvpPresenter

class PresenterChackNorris : MvpPresenter<ViewChackNorris>() {
    fun loadQuotes() {
        viewState.load()
        ProviderChackNorris(this).loadQuotesEn()
    }

    fun completeLoadingEn(
        icon: String,
        quoteEn: String
    ) {
        viewState.completeLoadingEn(icon = icon, quoteEn = quoteEn)
        viewState.saveQuote()
    }

    fun completeLoadingRu(
        quoteRu: String
    ) {
        viewState.completeLoadingRu(quoteRu = quoteRu)
        viewState.saveQuote()
    }

    fun errorLoading(textError: String?) {
        viewState.errorLoad(textError)
    }

    fun refreshLoadQuotes() {
        ProviderChackNorris(this).loadQuotesEn()
    }

    fun translateText(txt: String, author: String) {
        viewState.load()
        viewState.translateQuote()
        ProviderChackNorris(this).loadTranslate(text = txt)

    }
}