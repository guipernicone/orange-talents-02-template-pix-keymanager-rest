package br.com.zup.controller

import br.com.zup.controller.request.RegisterPixRequest
import br.com.zup.controller.resource.AccountTypeRequest
import br.com.zup.controller.resource.PixType
import br.com.zup.grpc.PixManagerGrpcFactory
import br.com.zup.utils.MockitoHelper
import com.zup.br.orange.PixKeyRegisterGrpcResponse
import com.zup.br.orange.PixKeyRegisterServiceGrpc
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class PixRegisterControllerTest {

    @field: Inject
    lateinit var registerStub: PixKeyRegisterServiceGrpc.PixKeyRegisterServiceBlockingStub

    @field: Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testRegisterPix(){
        val clientId = UUID.randomUUID()
        val registerPixRequest = RegisterPixRequest(
            PixType.EMAIL,
            "test@test.com",
            AccountTypeRequest.CONTA_CORRENTE
        )

        val grpcClientResponse = PixKeyRegisterGrpcResponse.newBuilder()
            .setClientId(clientId.toString())
            .setPixId("IdTeste")
            .build()

        Mockito.`when`(registerStub.register(MockitoHelper.anyObject())).thenReturn(grpcClientResponse)

        val httpRequest = HttpRequest.POST("/api/v1/clients/$clientId/pix/", registerPixRequest)
        val response = client.toBlocking().exchange(httpRequest, RegisterPixRequest::class.java)

        with(response){
            Assertions.assertEquals(HttpStatus.CREATED, response.status)
            Assertions.assertTrue(response.headers.contains("Location"))
            Assertions.assertTrue(response.header("Location")!!.contains("IdTeste"))
        }
    }


    @Factory
    @Replaces(factory = PixManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun registerMock() = Mockito.mock(PixKeyRegisterServiceGrpc.PixKeyRegisterServiceBlockingStub::class.java)
    }


}