package br.com.zup.controller

import br.com.zup.controller.request.RegisterPixRequest
import br.com.zup.grpc.PixManagerGrpcFactory
import com.zup.br.orange.PixKeyRegisterServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller("api/v1/clients/{clientId}/pix")
class PixController(
    private val grpcPixService: PixKeyRegisterServiceGrpc.PixKeyRegisterServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post
    fun registerPix(clientId: String, @Valid @Body request: RegisterPixRequest) : HttpResponse<Any>{

        logger.info("Register request receive from client $clientId with request: \n ${request.toString()}")

        val grpcResponse = grpcPixService.register(request.toGrpcModel(clientId))
        return HttpResponse.created(this.location(clientId, grpcResponse.pixId))
    }

    private fun location(clientId: String, pixId: String) = HttpResponse.uri("api/v1/clients/$clientId/pix/$pixId")

}