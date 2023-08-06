package br.com.advisor.domain.mapper

import br.com.advisor.data.dto.MessageDTO
import br.com.advisor.domain.model.Message

fun MessageDTO.toDomain() = Message(
    type = type,
    text = text
)
