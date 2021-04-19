package br.com.zup.controller;

import br.com.zup.controller.resource.AccountTypeRequest
import br.com.zup.controller.resource.PixType
import br.com.zup.controller.response.ListPixPixResponse
import br.com.zup.controller.response.ListPixResponse
import com.zup.br.orange.PixKeyListGrpcRequest
import com.zup.br.orange.PixKeyListGrpcResponse
import com.zup.br.orange.PixKeyListServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory

@Controller("api/v1/clients/{clientId}/pix")
class ListPixController(
    private val grpcPixListService: PixKeyListServiceGrpc.PixKeyListServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get
    fun listPix(clientId: String): HttpResponse<ListPixResponse> {
        logger.info("List request receive from client $clientId")

        val response = grpcPixListService.list(PixKeyListGrpcRequest.newBuilder().setClientId(clientId).build())

        return HttpResponse.ok(buildResponse(response))
    }

    private fun buildResponse(response: PixKeyListGrpcResponse) : ListPixResponse {
        val pixList = response.pixListList.map { ListPixPixResponse(
            it.pixId,
            PixType.valueOf(it.pixType.name),
            it.pixValue,
            AccountTypeRequest.valueOf(it.accountType.name),
            it.createdAt
        ) }

        return ListPixResponse(response.clientId, pixList)
    }

}
