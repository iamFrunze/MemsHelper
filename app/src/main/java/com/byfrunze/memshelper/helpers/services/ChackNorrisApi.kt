package com.byfrunze.memshelper.helpers.services

import com.byfrunze.memshelper.helpers.Results
import io.reactivex.Observable
import retrofit2.http.GET

interface ChackNorrisApi {
    @GET("jokes/random")
    fun chackNorritsQuote(): Observable<Results.ChackNorrisQuotes>
}