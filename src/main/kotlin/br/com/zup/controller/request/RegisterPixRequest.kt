package br.com.zup.controller.request

import br.com.zup.controller.resource.AccountTypeRequest
import br.com.zup.controller.resource.PixType
import br.com.zup.validation.ValidPix
import com.zup.br.orange.AccountType
import com.zup.br.orange.KeyType
import com.zup.br.orange.PixKeyRegisterGrpcRequest
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPix
data class RegisterPixRequest(
    @field:NotNull val keyType: PixType?,
    @field:Size(max = 77) val keyValue: String?,
    @field:NotNull val accountType: AccountTypeRequest?
) {
    fun toGrpcModel(clientId: String): PixKeyRegisterGrpcRequest {
        return PixKeyRegisterGrpcRequest.newBuilder()
            .setClientId(clientId)
            .setKeyType(KeyType.valueOf(this.keyType!!.name))
            .setKeyValue(this.keyValue)
            .setAccount(AccountType.valueOf(this.accountType!!.name))
            .build()
    }

}