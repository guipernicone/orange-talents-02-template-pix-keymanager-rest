package br.com.zup.controller.response

import br.com.zup.controller.resource.AccountTypeRequest
import br.com.zup.controller.resource.PixType

data class ListPixPixResponse(
    val pixId: String,
    val pixType: PixType,
    val pixValue: String,
    val accountTypeRequest: AccountTypeRequest,
    val createdAt: String
) {
}