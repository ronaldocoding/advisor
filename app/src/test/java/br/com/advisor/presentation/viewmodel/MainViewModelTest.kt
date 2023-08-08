package br.com.advisor.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.advisor.R
import br.com.advisor.base.Result
import br.com.advisor.test_rule.CoroutineTestRule
import br.com.advisor.base.getOrAwaitValue
import br.com.advisor.domain.model.Advice
import br.com.advisor.domain.usecase.GetAdviceUseCase
import br.com.advisor.presentation.action.MainAction
import br.com.advisor.presentation.model.MainUiModel
import br.com.advisor.presentation.state.MainUiState
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val useCase = mockk<GetAdviceUseCase>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(useCase)
    }

    @Test
    fun `WHEN view model is created THEN uiState must be the Initial state`() =
        coroutineTestRule.runBlockingTest {
            val supportingTextResource = R.string.initial_supporting_text
            val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
            val expectedState = MainUiState.Initial(uiModel)
            val actualState = viewModel.uiState.getOrAwaitValue()

            assertTrue(actualState == expectedState)
            assertTrue(
                expectedState.uiModel.supportingTextResource ==
                        (actualState as MainUiState.Initial).uiModel.supportingTextResource
            )
        }

    @Test
    fun `GIVEN OnClickGetAdviceButton action was sent WHEN sendAction is called THEN must emit the Loading state`() =
        coroutineTestRule.runBlockingTest {
            viewModel.sendAction(MainAction.Action.OnClickGetAdviceButton)

            val expectedUiState = MainUiState.Loading

            val actualUiState = viewModel.uiState.getOrAwaitValue()

            assertTrue(actualUiState == expectedUiState)
        }

    @Test
    fun `GIVEN OnGetDevice was sent and successful result WHEN execute use case THEN must emit the Advice state`() =
        coroutineTestRule.runBlockingTest {
            val supportingTextResource = R.string.advice_supporting_text
            val adviceText = "Advice"
            val uiModel = MainUiModel(
                supportingTextResource = supportingTextResource,
                adviceText = adviceText
            )
            val expectedState = MainUiState.Advice(uiModel)

            coEvery { useCase.execute() } returns Result.Success(Advice(adviceText))

            viewModel.sendAction(MainAction.Action.OnGetAdvice)

            val actualState = viewModel.uiState.getOrAwaitValue()

            assertTrue(actualState == expectedState)
            assertTrue(
                expectedState.uiModel.supportingTextResource ==
                        (actualState as MainUiState.Advice).uiModel.supportingTextResource
            )
            assertTrue(
                expectedState.uiModel.adviceText ==
                        actualState.uiModel.adviceText
            )
        }

    @Test
    fun `GIVEN OnGetDevice was sent and unsuccessful result WHEN execute use case THEN must emit the Error state`() =
        coroutineTestRule.runBlockingTest {
            val supportingTextResource = R.string.error_supporting_text
            val uiModel = MainUiModel(supportingTextResource = supportingTextResource)
            val expectedState = MainUiState.Error(uiModel)

            coEvery { useCase.execute() } returns Result.Error(Exception())

            viewModel.sendAction(MainAction.Action.OnGetAdvice)

            val actualState = viewModel.uiState.getOrAwaitValue()

            assertTrue(actualState == expectedState)
            assertTrue(
                expectedState.uiModel.supportingTextResource ==
                        (actualState as MainUiState.Error).uiModel.supportingTextResource
            )
        }
}
