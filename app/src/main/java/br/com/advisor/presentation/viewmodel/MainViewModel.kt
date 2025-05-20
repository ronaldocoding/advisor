package br.com.advisor.presentation.viewmodel

import android.util.Log
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
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainViewModel(private val useCase: GetAdviceUseCase) : ViewModel(), MainAction {

    private val supportingTextResource = R.string.initial_supporting_text
    private val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
    private lateinit var englishPortugueseTranslator: Translator
    private val initialState = MainUiState.Loading
    private val _uiState = MutableLiveData<MainUiState>(initialState)
    val uiState = _uiState as LiveData<MainUiState>

    override fun sendAction(action: MainAction.Action) {
        viewModelScope.launch {
            when (action) {
                is MainAction.Action.OnInit -> handleOnInit()
                is MainAction.Action.OnClickGetAdviceButton -> handleOnClickGetAdviceButton()
                is MainAction.Action.OnGetAdvice -> handleOnGetDevice()
            }
        }
    }

    private fun handleOnInit() {
        _uiState.value = MainUiState.Loading
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.PORTUGUESE)
            .build()
        englishPortugueseTranslator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        englishPortugueseTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                _uiState.value = MainUiState.Initial(uiModel)
            }
            .addOnFailureListener {
                handleErrorState()
            }
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
        var adviceText = advice.text
        Log.d("Translate", "Language: ${Locale.getDefault().language}")
        if (Locale.getDefault().language == "pt") {
            englishPortugueseTranslator.translate(advice.text)
                .addOnSuccessListener { translatedText ->
                    Log.d("Translate", "Texto foi traduzido")
                    adviceText = translatedText
                    val uiModel = MainUiModel(
                        supportingTextResource = supportingTextResource,
                        adviceText = adviceText
                    )
                    val uiState = MainUiState.Advice(uiModel)
                    _uiState.value = uiState
                }
                .addOnFailureListener {
                    Log.d("Translate", "Erro: ${it.message}")
                    handleErrorState()
                }
        } else {
            val uiModel = MainUiModel(
                supportingTextResource = supportingTextResource,
                adviceText = adviceText
            )
            val uiState = MainUiState.Advice(uiModel)
            _uiState.value = uiState
        }
    }

    private fun handleErrorState() {
        val supportingTextResource = R.string.error_supporting_text
        val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
        val uiState = MainUiState.Error(uiModel)
        _uiState.value = uiState
    }
}