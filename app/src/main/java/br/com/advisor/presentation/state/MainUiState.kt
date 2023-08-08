package br.com.advisor.presentation.state

import br.com.advisor.presentation.model.MainUiModel

sealed class MainUiState {
    data class Initial(val uiModel: MainUiModel) : MainUiState()
    data class Advice(val uiModel: MainUiModel) : MainUiState()
    data class Error(val uiModel: MainUiModel) : MainUiState()
    object Loading : MainUiState()
}
