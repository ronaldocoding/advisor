package br.com.advisor.data

import android.util.Log
import br.com.advisor.base.Result
import br.com.advisor.data.api.AdviceSlipApi
import br.com.advisor.domain.mapper.toDomain
import br.com.advisor.domain.model.Advice
import br.com.advisor.domain.repository.AdviceRepository

class AdviceRepositoryImpl(private val api: AdviceSlipApi) : AdviceRepository {

    override suspend fun getAdvice(): Result<Advice> {
        return try {
            val result = api.getAdvice()
            if (result.isSuccessful) {
                result.body()?.let {
                    Result.Success(it.advice.toDomain())
                } ?: Result.Error(Exception("Request with empty body"))
            } else {
                Result.Error(Exception("Request failed with code: ${result.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(Exception("Request failed with exception: ${e.message}"))
        }
    }
}
