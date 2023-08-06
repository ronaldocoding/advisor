package br.com.advisor.domain.mapper

import br.com.advisor.data.dto.MessageDTO
import br.com.advisor.domain.model.Message
import org.junit.Assert.assertEquals
import org.junit.Test

class MessageMapperTest {

    @Test
    fun `GIVEN a MessageDTO WHEN toDomain is called THEN must return the expected Message`() {
        val messageDTO = MessageDTO(type = "type", text = "text")

        val expectedMessage = Message(type = "type", text = "text")

        val actualMessage = messageDTO.toDomain()

        assertEquals(expectedMessage.type, actualMessage.type)
        assertEquals(expectedMessage.text, actualMessage.text)
    }
}
