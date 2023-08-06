package br.com.advisor.data.dto

import com.google.gson.annotations.SerializedName

data class MessageDTO(
    @SerializedName("type")
    val type: String,
    @SerializedName("text")
    val text: String
)
