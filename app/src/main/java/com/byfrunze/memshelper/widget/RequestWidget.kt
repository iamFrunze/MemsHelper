package com.byfrunze.memshelper.widget

import com.byfrunze.memshelper.helpers.Results
import com.byfrunze.memshelper.helpers.RetroService
import com.byfrunze.memshelper.helpers.services.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RequestWidget {

    private val METHOD = "getQuote"
    private val FORMAT = "json"
    private val LANG_EN = "en"

    private val YA_API_KEY =
        "trnsl.1.1.20200503T142202Z.f64ffd2b72e86ce5.3d006d53255b6d52717ad4b6372b8068c0100f6b"
    private val YA_LANG = "en-ru"

    private val apiServiceAdvice by lazy {
        RetroService.createRetrofit("https://api.adviceslip.com/")
            .create(AdviceSlipApi::class.java)
    }
    private val apiServiceTranslate by lazy {
        RetroService.createRetrofit("https://translate.yandex.net/")
            .create(TranslateService::class.java)
    }
    private val apiServiceChack by lazy {
        RetroService.createRetrofit("https://api.chucknorris.io/")
            .create(ChackNorrisApi::class.java)
    }
    private val apiServiceMotivation by lazy {
        RetroService.createRetrofit("http://forismatic.com/api/").create(MotivationApi::class.java)
    }
    private val apiServiceProg by lazy {
        RetroService.createRetrofit("https://programming-quotes-api.herokuapp.com/")
            .create(ProgrammingApi::class.java)
    }

    fun loadQuotesAdvice(): Observable<Results.AdviceSlipQuotes> {
        return apiServiceAdvice.adviceSlipQuotes()
    }
    fun loadQuotesChack(): Observable<Results.ChackNorrisQuotes> {
        return apiServiceChack.chackNorritsQuote()
    }
    fun loadQuotesProg(): Observable<Results.ProgrammingQuotes> {
        return apiServiceProg.programmingQuotes()
    }
    fun loadQuotesMot(): Observable<Results.MotivationQuotes> {
        return apiServiceMotivation.createMotivationQuotes(
            method = METHOD,
            format = FORMAT,
            lang = LANG_EN
        )
    }

    fun loadTranslate(text: String): Observable<Results.TranslateQuotes> {
        return apiServiceTranslate.translate(
            key = YA_API_KEY,
            text = text,
            lang = YA_LANG
        )
    }
}