package br.com.advisor.domain.repository

import br.com.advisor.base.Result
import br.com.advisor.domain.model.Advice

interface AdviceRepository {
    suspend fun getAdvice(): Result<Advice>
}