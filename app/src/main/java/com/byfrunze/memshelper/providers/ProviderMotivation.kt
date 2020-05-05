package com.byfrunze.memshelper.providers

import com.byfrunze.memshelper.helpers.services.MotivationApi
import com.byfrunze.memshelper.helpers.RetroService
import com.byfrunze.memshelper.helpers.services.TranslateService
import com.byfrunze.memshelper.presenters.PresenterMotivation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ProviderMotivation(val presenter: PresenterMotivation) {

    private val METHOD = "getQuote"
    private val FORMAT = "json"
    private val LANG_EN = "en"
    private val LANG_RU = "ru"

    private val YA_API_KEY =
        "trnsl.1.1.20200503T142202Z.f64ffd2b72e86ce5.3d006d53255b6d52717ad4b6372b8068c0100f6b"
    private val YA_LANG = "en-ru"

    private val apiServiceQuote by lazy {
        RetroService.createRetrofit("http://forismatic.com/api/").create(MotivationApi::class.java)
    }
    private val apiServiceTranslate by lazy {
        RetroService.createRetrofit("https://translate.yandex.net/")
            .create(TranslateService::class.java)
    }

    private var disposable: Disposable? = null

    fun loadQuotesEn() {
        disposable = apiServiceQuote.createMotivationQuotes(
            method = METHOD,
            format = FORMAT,
            lang = LANG_EN
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    presenter.completeLoadingEn(
                        quoteEn = it.quoteText,
                        quoteAuthorEn = it.quoteAuthor
                    )
                    loadTranslate(it.quoteText, quoteAuthor = it.quoteAuthor)
                },
                {
                    presenter.errorLoading(textError = it.localizedMessage)
                }
            )
    }

    private fun loadTranslate(quoteEn: String, quoteAuthor: String) {
        disposable = apiServiceTranslate.translate(
            key = YA_API_KEY,
            text = quoteEn,
            lang = YA_LANG
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    presenter.completeLoadingRu(
                        quoteRu = it.text[0],
                        quoteAuthorRu = quoteAuthor
                    )
                },
                {
                    presenter.errorLoading(textError = it.localizedMessage)
                }
            )
    }
}