package br.com.zup.controller.response

import br.com.zup.controller.resource.AccountTypeRequest

class ConsultPixAccountResponse(
    val type: AccountTypeRequest,
    val institution: String,
    val ispb: String,
    val titular: String,
    val cpf: String,
    val agency: String,
    val accountNumber: String
) {
}