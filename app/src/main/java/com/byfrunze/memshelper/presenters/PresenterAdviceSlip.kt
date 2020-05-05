package com.byfrunze.memshelper.presenters

import com.byfrunze.memshelper.providers.ProviderAdviceSlip
import com.byfrunze.memshelper.views.ViewAdviceSlip
import moxy.MvpPresenter

class PresenterAdviceSlip : MvpPresenter<ViewAdviceSlip>() {
    fun loadQuotes() {
        viewState.load()
        ProviderAdviceSlip(this).loadQuotesEn()
    }

    fun completeLoadingEn(quoteEn: String) {
        viewState.completeLoadingEn(quoteEn = quoteEn)
    }

    fun errorLoading(textError: String?) {
        viewState.errorLoad(textError = textError)
    }

    fun completeLoadingRu(quoteRu: String) {
        viewState.completeLoadingRu(quoteRu = quoteRu)
        viewState.saveQuote()
    }

    fun refreshLoadQuotes() {
        ProviderAdviceSlip(this).loadQuotesEn()
    }

}