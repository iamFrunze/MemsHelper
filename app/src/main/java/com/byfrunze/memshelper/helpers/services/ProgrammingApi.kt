package com.byfrunze.memshelper.helpers.services

import com.byfrunze.memshelper.helpers.Results
import io.reactivex.Observable
import retrofit2.http.GET

interface ProgrammingApi {
    @GET("quotes/random")
    fun programmingQuotes(): Observable<Results.ProgrammingQuotes>
}