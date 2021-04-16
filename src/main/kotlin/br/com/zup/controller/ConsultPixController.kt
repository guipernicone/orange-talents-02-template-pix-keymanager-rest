package br.com.zup.controller

import br.com.zup.controller.resource.AccountTypeRequest
import br.com.zup.controller.resource.PixType
import br.com.zup.controller.response.ConsultPixAccountResponse
import br.com.zup.controller.response.ConsultPixPixResponse
import br.com.zup.controller.response.ConsultPixResponse
import com.zup.br.orange.AccountType
import com.zup.br.orange.PixKeyConsultGrpcRequest
import com.zup.br.orange.PixKeyConsultGrpcResponse
import com.zup.br.orange.PixKeyConsultServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

@Validated
@Controller("api/v1/clients/{clientId}/pix")
class ConsultPixController(
    val grpcPixConsultService : PixKeyConsultServiceGrpc.PixKeyConsultServiceBlockingStub
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Get("/{pixId}")
    fun consultPix(clientId: String, pixId: String): HttpResponse<ConsultPixResponse> {
        logger.info("Consult request receive from client $clientId with pix Id: $pixId")

        val response =  grpcPixConsultService.consult(createRequest(clientId, pixId))

        return HttpResponse.ok(createResponse(response))

    }

    private fun createRequest(clientId: String, pixId: String) : PixKeyConsultGrpcRequest{
        val pixIdRequest = PixKeyConsultGrpcRequest.newBuilder().pixIdBuilder
            .setPixId(pixId)
            .setClientId(clientId)

        return PixKeyConsultGrpcRequest.newBuilder()
            .setPixId(pixIdRequest)
            .build()

    }

    private fun createResponse(response: PixKeyConsultGrpcResponse): ConsultPixResponse {
        val account = ConsultPixAccountResponse(
            AccountTypeRequest.valueOf(response.pix.account.type.name),
            response.pix.account.institution,
            response.pix.account.ispb,
            response.pix.account.titular,
            response.pix.account.cpf,
            response.pix.account.agency,
            response.pix.account.accountNumber,
        )

        val pix = ConsultPixPixResponse(
            PixType.valueOf(response.pix.type),
            response.pix.key,
            account,
            response.pix.createdAt
        )

        return ConsultPixResponse(
            response.clientId,
            response.pixId,
            pix
        )
    }

}