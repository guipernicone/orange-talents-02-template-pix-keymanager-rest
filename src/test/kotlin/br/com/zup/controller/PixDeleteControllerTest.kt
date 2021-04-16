package br.com.zup.controller

import br.com.zup.controller.request.RegisterPixRequest
import br.com.zup.controller.resource.AccountTypeRequest
import br.com.zup.controller.resource.PixType
import br.com.zup.controller.response.DeletePixResponse
import br.com.zup.grpc.PixManagerGrpcFactory
import com.zup.br.orange.PixKeyDeleteGrpcResponse
import com.zup.br.orange.PixKeyDeleteServiceGrpc
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
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
class PixDeleteControllerTest {

    @field:Inject
    lateinit var deleteStub: PixKeyDeleteServiceGrpc.PixKeyDeleteServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testRegisterPix(){
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        val deletedAt = LocalDateTime.now().toString()

        val grpcClientResponse = PixKeyDeleteGrpcResponse.newBuilder()
            .setClientId(clientId)
            .setDeletedAt(deletedAt)
            .build()

        Mockito.`when`(deleteStub.delete(Mockito.any())).thenReturn(grpcClientResponse)

        val httpRequest = HttpRequest.DELETE<Any>("/api/v1/clients/$clientId/pix/$pixId")

        val response = client.toBlocking().exchange(httpRequest, DeletePixResponse::class.java)

        with(response){
            Assertions.assertEquals(HttpStatus.OK, response.status)
            Assertions.assertNotNull(response.body())
            Assertions.assertEquals(clientId, response.body()?.clientId)
            Assertions.assertEquals(deletedAt, response.body()?.deletedAt)
        }
    }

    @Factory
    @Replaces(factory = PixManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun stubMock() = Mockito.mock(PixKeyDeleteServiceGrpc.PixKeyDeleteServiceBlockingStub::class.java)
    }
}