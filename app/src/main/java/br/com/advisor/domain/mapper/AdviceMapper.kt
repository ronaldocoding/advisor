package br.com.advisor.domain.mapper

import br.com.advisor.data.dto.AdviceDTO
import br.com.advisor.domain.model.Advice

fun AdviceDTO.toDomain() = Advice(
    text = text
)
