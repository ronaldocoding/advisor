package br.com.advisor.data

import br.com.advisor.base.Result
import br.com.advisor.data.api.AdviceSlipApi
import br.com.advisor.data.dto.AdviceDTO
import br.com.advisor.data.dto.MessageDTO
import br.com.advisor.domain.mapper.toDomain
import br.com.advisor.domain.model.Advice
import br.com.advisor.domain.repository.AdviceRepository

class AdviceRepositoryImpl(private val api: AdviceSlipApi) : AdviceRepository {

    override suspend fun getAdvice(): Result<Advice> {
        val result = api.getAdvice()
        return if (result.isSuccessful) {
            result.body()?.let {
                if (it is AdviceDTO) {
                    Result.Success(it.toDomain())
                } else {
                    val messageDTO = it as MessageDTO
                    val message = messageDTO.toDomain()
                    Result.Error(Exception("Type: ${message.type} - Message: ${message.text}"))
                }
            } ?: Result.Error(Exception("Request with empty body"))
        } else {
            Result.Error(Exception("Request failed with code: ${result.code()}"))
        }
    }
}