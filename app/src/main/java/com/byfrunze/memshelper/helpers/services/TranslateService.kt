package com.byfrunze.memshelper.helpers.services

import com.byfrunze.memshelper.helpers.Results
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TranslateService {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    fun translate(
        @Field("key") key: String,
        @Field("text") text: String,
        @Field("lang") lang: String
    ): Observable<Results.TranslateQuotes>
}