package com.byfrunze.memshelper.helpers.services

import com.byfrunze.memshelper.helpers.Results
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MotivationApi {

    @FormUrlEncoded
    @POST("1.0/")
    fun createMotivationQuotes(
        @Field("method") method: String,
        @Field("format") format: String,
        @Field("lang") lang: String
    ): Observable<Results.MotivationQuotes>
}