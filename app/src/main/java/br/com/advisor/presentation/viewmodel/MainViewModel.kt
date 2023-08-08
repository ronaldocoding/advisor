package br.com.advisor.presentation.viewmodel

import androidx.lifecycle.LiveData
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

    private val supportingTextResource = R.string.initial_supporting_text
    private val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
    private val initialState = MainUiState.Initial(uiModel)
    private val _uiState = MutableLiveData<MainUiState>(initialState)
    val uiState = _uiState as LiveData<MainUiState>

    override fun sendAction(action: MainAction.Action) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (action) {
                    is MainAction.Action.OnClickGetAdviceButton -> handleOnClickGetAdviceButton()
                    is MainAction.Action.OnGetAdvice -> handleOnGetDevice()
                }
            }
        }
    }

    private suspend fun handleOnClickGetAdviceButton() {
        withContext(Dispatchers.Main) {
            _uiState.value = MainUiState.Loading
        }
    }

    private suspend fun handleOnGetDevice() {
        when (val result = useCase.execute()) {
            is Result.Success -> handleAdviceState(result.data)
            is Result.Error -> handleErrorState()
        }

    }

    private suspend fun handleAdviceState(advice: Advice) {
        val supportingTextResource = R.string.advice_supporting_text
        val adviceText = advice.text
        val uiModel = MainUiModel(
            supportingTextResource = supportingTextResource,
            adviceText = adviceText
        )
        val uiState = MainUiState.Advice(uiModel)
        withContext(Dispatchers.Main) {
            _uiState.value = uiState
        }
    }

    private suspend fun handleErrorState() {
        val supportingTextResource = R.string.error_supporting_text
        val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
        val uiState = MainUiState.Error(uiModel)
        withContext(Dispatchers.Main) {
            _uiState.value = uiState
        }
    }
}