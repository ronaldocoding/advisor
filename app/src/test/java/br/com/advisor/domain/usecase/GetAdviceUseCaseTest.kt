package br.com.advisor.domain.usecase

import br.com.advisor.base.Result
import br.com.advisor.domain.model.Advice
import br.com.advisor.domain.repository.AdviceRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAdviceUseCaseTest {

    private val repository = mockk<AdviceRepository>()

    private lateinit var useCase: GetAdviceUseCase

    @Before
    fun setUp() {
        useCase = GetAdviceUseCase(repository)
    }

    @Test
    fun `GIVEN a successful result WHEN execute use case THEN must returns the expected success result`() =
        runTest {
            val advice = Advice(text = "text")
            val expectedResult = Result.Success(advice)

            coEvery { repository.getAdvice() } returns expectedResult

            val actualResult = useCase.execute()

            assertTrue(actualResult is Result.Success)
            assertEquals(expectedResult.data.text, (actualResult as Result.Success).data.text)
        }

    @Test
    fun `GIVEN an error result WHEN execute use case THEN must returns the expected error result`() =
        runTest {
            val exception = Exception("Error message")
            val expectedResult = Result.Error(exception)

            coEvery { repository.getAdvice() } returns expectedResult

            val actualResult = useCase.execute()

            assertTrue(actualResult is Result.Error)
            assertEquals(
                expectedResult.exception.message, (actualResult as Result.Error).exception.message
            )
        }
}