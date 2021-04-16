package br.com.zup.controller.response

import br.com.zup.controller.resource.PixType

class ConsultPixPixResponse(
    val type: PixType,
    val key: String,
    val account: ConsultPixAccountResponse,
    val createdAt: String
){
}