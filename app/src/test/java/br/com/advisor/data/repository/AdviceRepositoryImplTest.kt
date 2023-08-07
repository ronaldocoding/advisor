package br.com.advisor.data.repository

import br.com.advisor.base.Result
import br.com.advisor.data.AdviceRepositoryImpl
import br.com.advisor.data.api.AdviceSlipApi
import br.com.advisor.data.dto.AdviceDTO
import br.com.advisor.data.dto.MessageDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.internal.builders.NullBuilder
import retrofit2.Response

class AdviceRepositoryImplTest {

    private val api = mockk<AdviceSlipApi>()

    private lateinit var repository: AdviceRepositoryImpl

    @Before
    fun setUp() {
        repository = AdviceRepositoryImpl(api)
    }

    @Test
    fun `GIVEN an AdviceDTO WHEN getAdvice is called THEN must return the expected success result`() =
        runTest {
            val adviceDTO = AdviceDTO(id = 1, text = "advice")

            coEvery { api.getAdvice() } returns Response.success(adviceDTO)

            val expectedResult = Result.Success(adviceDTO)
            val actualResult = repository.getAdvice()

            assertTrue(actualResult is Result.Success)
            assertEquals(expectedResult.data.text, (actualResult as Result.Success).data.text)
        }

    @Test
    fun `GIVEN a MessageDTO WHEN getAdvice is called THEN must return the expected error result`() =
        runTest {
            val messageDTO = MessageDTO(type = "Error", text = "Error message")
            val exception = Exception("Type: ${messageDTO.type} - Message: ${messageDTO.text}")

            coEvery { api.getAdvice() } returns Response.success(messageDTO)

            val expectedResult = Result.Error(exception)
            val actualResult = repository.getAdvice()

            assertTrue(actualResult is Result.Error)
            assertEquals(
                expectedResult.exception.message,
                (actualResult as Result.Error).exception.message
            )
        }

    @Test
    fun `GIVEN a request error WHEN getAdvice is called THEN must return the expected error result`() =
        runTest {
            val exception = Exception("Request failed with code: 500")

            coEvery { api.getAdvice() } returns Response.error(500, ResponseBody.create(null, ""))

            val expectedResult = Result.Error(exception)
            val actualResult = repository.getAdvice()

            assertTrue(actualResult is Result.Error)
            assertEquals(
                expectedResult.exception.message,
                (actualResult as Result.Error).exception.message
            )
        }

    @Test
    fun `GIVEN a successful request with empty body WHEN getAdvice is called THEN must return the expected error result`() =
        runTest {
            val exception = Exception("Request with empty body")

            coEvery { api.getAdvice() } returns Response.success(null)

            val expectedResult = Result.Error(exception)
            val actualResult = repository.getAdvice()

            assertTrue(actualResult is Result.Error)
            assertEquals(
                expectedResult.exception.message,
                (actualResult as Result.Error).exception.message
            )
        }
}
