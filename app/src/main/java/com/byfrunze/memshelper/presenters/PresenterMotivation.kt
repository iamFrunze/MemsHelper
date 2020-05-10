package com.byfrunze.memshelper.presenters

import com.byfrunze.memshelper.providers.ProviderMotivation
import com.byfrunze.memshelper.views.ViewMotivation
import moxy.MvpPresenter

class PresenterMotivation : MvpPresenter<ViewMotivation>() {
    fun loadQuotes() {
        viewState.load()
        ProviderMotivation(this).loadQuotesEn()
    }

    fun completeLoadingEn(
        quoteEn: String = "",
        quoteAuthorEn: String = ""
    ) {
        viewState.completeLoadingEn(quoteEn = quoteEn, quoteAuthorEn = quoteAuthorEn)
        viewState.saveQuote()

    }

    fun completeLoadingRu(
        quoteRu: String = "",
        quoteAuthorRu: String = ""
    ) {
        viewState.completeLoadingRu(quoteRu = quoteRu, quoteAuthorRu = quoteAuthorRu)
        viewState.saveQuote()

    }

    fun errorLoading(textError: String?) {
        viewState.errorLoad(textError)
    }

    fun refreshLoadQuotes() {
        ProviderMotivation(this).loadQuotesEn()
    }

    fun translateText(txt: String, author: String) {
        viewState.load()
        viewState.translateQuote()
        ProviderMotivation(this).loadTranslate(quoteEn = txt, quoteAuthor = author)

    }

}