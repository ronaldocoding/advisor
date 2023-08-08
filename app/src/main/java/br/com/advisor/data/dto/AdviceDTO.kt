package br.com.advisor.data.dto

import com.google.gson.annotations.SerializedName


data class SlipDTO(
    @SerializedName("slip")
    val advice: AdviceDTO
)
data class AdviceDTO(
    @SerializedName("slip_id")
    val id: Int,
    @SerializedName("advice")
    val text: String
)
