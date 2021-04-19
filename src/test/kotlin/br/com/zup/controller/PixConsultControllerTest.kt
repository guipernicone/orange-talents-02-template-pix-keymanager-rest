package br.com.zup.controller

import br.com.zup.controller.response.ConsultPixResponse
import br.com.zup.controller.response.DeletePixResponse
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
import kotlin.io.path.createTempDirectory

@MicronautTest
internal class PixConsultControllerTest {

    @field:Inject
    lateinit var consultStub: PixKeyConsultServiceGrpc.PixKeyConsultServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun consultPix(){
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        val createdAt = LocalDateTime.now().toString()

        val account =  PixKeyConsultGrpcResponse.newBuilder().pixBuilder.accountBuilder
            .setType(AccountType.CONTA_CORRENTE)
            .setAccountNumber("1")
            .setAgency("1")
            .setTitular("1")
            .setCpf("1")
            .setInstitution("1")
            .setIspb("1")
        val pix = PixKeyConsultGrpcResponse.newBuilder().pixBuilder
            .setCreatedAt(createdAt)
            .setType(KeyType.EMAIL.name)
            .setKey("1")
            .setAccount(account)
        val grpcResponse = PixKeyConsultGrpcResponse.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .setPix(pix)
            .build()

        Mockito.`when`(consultStub.consult(MockitoHelper.anyObject())).thenReturn(grpcResponse)

        val httpRequest = HttpRequest.GET<Any>("/api/v1/clients/$clientId/pix/$pixId")

        val response = client.toBlocking().exchange(httpRequest, ConsultPixResponse::class.java)

        with(response){
            Assertions.assertEquals(HttpStatus.OK, response.status)
            Assertions.assertNotNull(response.body())
            Assertions.assertEquals(clientId, response.body()?.clientId)
            Assertions.assertEquals(pixId, response.body()?.pixId)
            Assertions.assertEquals(createdAt, response.body()?.pix?.createdAt)
        }
    }

    @Factory
    @Replaces(factory = PixManagerGrpcFactory::class)
    internal class MockitoStubFactory{

        @Singleton
        fun consultMock() = Mockito.mock(PixKeyConsultServiceGrpc.PixKeyConsultServiceBlockingStub::class.java)
    }
}