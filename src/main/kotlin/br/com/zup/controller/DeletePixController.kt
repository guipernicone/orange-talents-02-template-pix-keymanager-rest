package br.com.zup.controller

import br.com.zup.controller.request.RegisterPixRequest
import br.com.zup.controller.response.DeletePixResponse
import com.zup.br.orange.PixKeyDeleteGrpcRequest
import com.zup.br.orange.PixKeyDeleteServiceGrpc
import com.zup.br.orange.PixKeyRegisterServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Controller("api/v1/clients/{clientId}/pix")
class DeletePixController(
    private val grpcPixDeleteService: PixKeyDeleteServiceGrpc.PixKeyDeleteServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Delete("/{pixId}")
    fun registerPix(clientId: String, pixId: String) : HttpResponse<Any>{

        logger.info("Delete request receive from client $clientId with pix Id: $pixId")

        val request = PixKeyDeleteGrpcRequest.newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .build()

        val grpcResponse = grpcPixDeleteService.delete(request)

        return HttpResponse.ok(DeletePixResponse(grpcResponse.clientId, grpcResponse.deletedAt))
    }
}