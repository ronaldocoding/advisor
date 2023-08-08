package br.com.advisor.data.api

import br.com.advisor.data.dto.SlipDTO
import retrofit2.Response
import retrofit2.http.GET

interface AdviceSlipApi {

    @GET("advice")
    suspend fun getAdvice(): Response<SlipDTO>
}
