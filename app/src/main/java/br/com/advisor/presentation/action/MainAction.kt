package br.com.advisor.presentation.action
interface MainAction {
    fun sendAction(action: Action)

    sealed class Action {
        object Initial : Action()
        object OnClickGetAdviceButton: Action()
        object OnGetAdvice: Action()
    }
}