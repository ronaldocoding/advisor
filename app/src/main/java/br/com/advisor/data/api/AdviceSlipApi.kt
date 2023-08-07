package br.com.advisor.data.api

import retrofit2.Response
import retrofit2.http.GET

interface AdviceSlipApi {

    @GET("advice")
    suspend fun getAdvice(): Response<Any>
}
