package br.com.zup.controller

import br.com.zup.controller.response.ConsultPixResponse
import br.com.zup.controller.response.ListPixResponse
import br.com.zup.grpc.PixManagerGrpcFactory
import br.com.zup.utils.MockitoHelper
import com.zup.br.orange.*
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
class PixListControllerTest {

    @Inject
    lateinit var listStub: PixKeyListServiceGrpc.PixKeyListServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun listPix(){
        val clientId = UUID.randomUUID().toString()
        val pix1 = UUID.randomUUID().toString()
        val pix2 = UUID.randomUUID().toString()
        val createdAt = LocalDateTime.now().toString()

        val pixItem1 = PixKeyListGrpcResponse.Pix.newBuilder()
            .setPixId("1")
            .setPixType(KeyType.RANDOM)
            .setPixValue(pix1)
            .setAccountType(AccountType.CONTA_CORRENTE)
            .setCreatedAt(createdAt)
            .build()
        val pixItem2 = PixKeyListGrpcResponse.Pix.newBuilder()
            .setPixId("1")
            .setPixType(KeyType.RANDOM)
            .setPixValue(pix2)
            .setAccountType(AccountType.CONTA_CORRENTE)
            .setCreatedAt(createdAt)
            .build()

        val grpcRespnse = PixKeyListGrpcResponse.newBuilder()
            .setClientId(clientId)
            .addPixList(pixItem1)
            .addPixList(pixItem2)
            .build()

        Mockito.`when`(listStub.list(MockitoHelper.anyObject())).thenReturn(grpcRespnse)

        val httpRequest = HttpRequest.GET<Any>("/api/v1/clients/$clientId/pix")

        val response = client.toBlocking().exchange(httpRequest, ListPixResponse::class.java)

        with(response){
            Assertions.assertEquals(HttpStatus.OK, response.status)
            Assertions.assertNotNull(response.body())
            Assertions.assertEquals(clientId, response.body()?.clientId)
            Assertions.assertNotNull(response.body()?.pixList)
            Assertions.assertEquals(2, response.body()?.pixList?.size)
            Assertions.assertTrue(arrayOf(pix1, pix2).contains(response.body()?.pixList?.get(0)?.pixValue))
            Assertions.assertTrue(arrayOf(pix1, pix2).contains(response.body()?.pixList?.get(1)?.pixValue))
        }
    }

    @Test
    fun listPixEmpty(){
        val clientId = UUID.randomUUID().toString()

        val grpcRespnse = PixKeyListGrpcResponse.newBuilder()
            .setClientId(clientId)
            .build()

        Mockito.`when`(listStub.list(MockitoHelper.anyObject())).thenReturn(grpcRespnse)

        val httpRequest = HttpRequest.GET<Any>("/api/v1/clients/$clientId/pix")

        val response = client.toBlocking().exchange(httpRequest, ListPixResponse::class.java)

        with(response){
            Assertions.assertEquals(HttpStatus.OK, response.status)
            Assertions.assertNotNull(response.body())
            Assertions.assertEquals(clientId, response.body()?.clientId)
            Assertions.assertNotNull(response.body()?.pixList)
            response.body()?.pixList?.isEmpty()?.let { Assertions.assertTrue(it) }
        }
    }

    @Factory
    @Replaces(factory = PixManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun listMock() = Mockito.mock(PixKeyListServiceGrpc.PixKeyListServiceBlockingStub::class.java)
    }
}