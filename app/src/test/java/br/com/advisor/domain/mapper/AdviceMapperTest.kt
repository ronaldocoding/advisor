package br.com.advisor.domain.mapper

import br.com.advisor.data.dto.AdviceDTO
import br.com.advisor.domain.model.Advice
import org.junit.Assert.assertEquals
import org.junit.Test

class AdviceMapperTest {

    @Test
    fun `GIVEN an AdviceDTO WHEN toDomain is called THEN must return the expected Advice`() {
        val adviceDTO = AdviceDTO(id = 1, text = "text")

        val expectedAdvice = Advice(text = "text")

        val actualAdvice = adviceDTO.toDomain()

        assertEquals(expectedAdvice.text, actualAdvice.text)
    }
}