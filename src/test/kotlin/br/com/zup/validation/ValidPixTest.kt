package br.com.zup.validation;

import br.com.zup.controller.request.RegisterPixRequest
import br.com.zup.controller.resource.AccountTypeRequest
import br.com.zup.controller.resource.PixType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class ValidPixTest {

    @Test
    fun testValidPixWithoutType(){
        val registerPixKeyRequest = RegisterPixRequest(
            null,
            "test@test.com",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixCpfFalse(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.CPF,
            "4783082707",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixCpfTrue(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.CPF,
            "47830827079",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixPhoneNull(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.PHONE,
            null,
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }
    @Test
    fun testValidPixPhoneFalse(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.PHONE,
            "88991606762",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixPhoneTrue(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.PHONE,
            "+5598991606762",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixEmailNull(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.EMAIL,
            null,
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }
    @Test
    fun testValidPixEmailFalse(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.EMAIL,
            "email",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixEmailTrue(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.EMAIL,
            "email@email.com",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixRandomFalse(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.RANDOM,
            "false",
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertFalse(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }

    @Test
    fun testValidPixRandomTrue(){
        val registerPixKeyRequest = RegisterPixRequest(
            PixType.RANDOM,
            null,
            AccountTypeRequest.CONTA_CORRENTE
        )
        Assertions.assertTrue(ValidPixValidator().isValid(registerPixKeyRequest, null, null))
    }
}
