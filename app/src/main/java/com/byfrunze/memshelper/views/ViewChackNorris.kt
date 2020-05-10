package com.byfrunze.memshelper.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ViewChackNorris: MvpView {
    fun load()
    fun errorLoad(textError: String?)
    fun completeLoadingEn(icon:String, quoteEn: String)
    fun completeLoadingRu(quoteRu: String)
    fun saveQuote()
    fun translateQuote()

}