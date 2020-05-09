package com.byfrunze.memshelper.providers

import com.byfrunze.memshelper.helpers.RetroService
import com.byfrunze.memshelper.helpers.services.ChackNorrisApi
import com.byfrunze.memshelper.helpers.services.MotivationApi
import com.byfrunze.memshelper.helpers.services.TranslateService
import com.byfrunze.memshelper.presenters.PresenterChackNorris
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProviderChackNorris(val presenter: PresenterChackNorris) {

    private val YA_API_KEY =
        "trnsl.1.1.20200503T142202Z.f64ffd2b72e86ce5.3d006d53255b6d52717ad4b6372b8068c0100f6b"
    private val YA_LANG = "en-ru"

    private val apiServiceQuote by lazy {
        RetroService.createRetrofit("https://api.chucknorris.io/")
            .create(ChackNorrisApi::class.java)
    }
    private val apiServiceTranslate by lazy {
        RetroService.createRetrofit("https://translate.yandex.net/")
            .create(TranslateService::class.java)
    }

    private var disposable: Disposable? = null

    fun loadQuotesEn() {
        disposable = apiServiceQuote.chackNorritsQuote()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    presenter.completeLoadingEn(
                        icon = it.icon_url,
                        quoteEn = it.value
                    )
                    loadTranslate(it.value)
                },
                {
                    presenter.errorLoading(textError = it.localizedMessage)
                }
            )
    }

    private fun loadTranslate(text: String) {
        disposable = apiServiceTranslate.translate(
            key = YA_API_KEY,
            text = text,
            lang = YA_LANG
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    presenter.completeLoadingRu(
                        quoteRu = it.text[0]
                    )
                },
                {
                    presenter.errorLoading(textError = it.localizedMessage)
                }
            )
    }
}