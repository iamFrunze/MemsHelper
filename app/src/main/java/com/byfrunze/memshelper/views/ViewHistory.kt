package com.byfrunze.memshelper.views

import com.byfrunze.memshelper.data.ModelSaveQuotes
import io.realm.RealmResults
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ViewHistory : MvpView {
    fun load()
    fun showEmptyHistory()
    fun completeLoading(model: RealmResults<ModelSaveQuotes>)
    fun errorLoading(textError: String?)

}