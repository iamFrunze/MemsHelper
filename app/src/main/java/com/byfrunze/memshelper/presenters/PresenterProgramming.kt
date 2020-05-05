package com.byfrunze.memshelper.presenters

import com.byfrunze.memshelper.providers.ProviderProgramming
import com.byfrunze.memshelper.views.ViewProgramming
import moxy.MvpPresenter

class PresenterProgramming : MvpPresenter<ViewProgramming>() {
    fun loadQuotes() {
        viewState.load()
        ProviderProgramming(this).loadQuotesEn()
    }

    fun completeLoadingEn(quoteEn: String, quoteAuthor: String) {
        viewState.completeLoadingEn(quoteEn = quoteEn, quoteAuthorEn = quoteAuthor)
    }

    fun errorLoading(textError: String?) {
        viewState.errorLoad(textError = textError)
    }

    fun completeLoadingRu(quoteRu: String, quoteAuthorRu: String) {
        viewState.completeLoadingRu(quoteRu = quoteRu, quoteAuthorRu = quoteAuthorRu)
        viewState.saveQuote()

    }

    fun refreshLoadQuotes() {
        ProviderProgramming(this).loadQuotesEn()
    }
}