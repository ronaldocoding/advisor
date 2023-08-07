package br.com.advisor.domain.usecase

import br.com.advisor.domain.repository.AdviceRepository

class GetAdviceUseCase(private val repository: AdviceRepository) {

    suspend fun execute() = repository.getAdvice()
}
