package br.com.advisor.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.advisor.R
import br.com.advisor.base.Result
import br.com.advisor.domain.model.Advice
import br.com.advisor.domain.usecase.GetAdviceUseCase
import br.com.advisor.presentation.action.MainAction
import br.com.advisor.presentation.model.MainUiModel
import br.com.advisor.presentation.state.MainUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val useCase: GetAdviceUseCase) : ViewModel(), MainAction {

    private val _uiState = MutableLiveData<MainUiState>()
    val uiState = _uiState.asFlow().asLiveData()

    override fun sendAction(action: MainAction.Action) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (action) {
                    is MainAction.Action.Initial -> handleInitial()
                    is MainAction.Action.OnClickGetAdviceButton -> handleOnClickGetAdviceButton()
                    is MainAction.Action.OnGetAdvice -> handleOnGetDevice()
                }
            }
        }
    }

    private fun handleInitial() {
        val supportingTextResource = R.string.initial_supporting_text
        val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
        val uiState = MainUiState.Initial(uiModel)
        _uiState.value = uiState
    }

    private fun handleOnClickGetAdviceButton() {
        _uiState.value = MainUiState.Loading
    }

    private suspend fun handleOnGetDevice() {
        when (val result = useCase.execute()) {
            is Result.Success -> handleAdviceState(result.data)
            is Result.Error -> handleErrorState()
        }

    }

    private fun handleAdviceState(advice: Advice) {
        val supportingTextResource = R.string.advice_supporting_text
        val adviceText = advice.text
        val uiModel = MainUiModel(
            supportingTextResource = supportingTextResource,
            adviceText = adviceText
        )
        val uiState = MainUiState.Advice(uiModel)
        _uiState.value = uiState
    }

    private fun handleErrorState() {
        val supportingTextResource = R.string.error_supporting_text
        val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
        val uiState = MainUiState.Error(uiModel)
        _uiState.value = uiState
    }
}