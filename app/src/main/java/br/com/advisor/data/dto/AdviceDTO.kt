package br.com.advisor.data.dto

import com.google.gson.annotations.SerializedName

data class AdviceDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("advice")
    val text: String
)
