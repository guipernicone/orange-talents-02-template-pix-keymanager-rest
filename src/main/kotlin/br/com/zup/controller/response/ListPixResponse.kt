package br.com.zup.controller.response

data class ListPixResponse(
    val clientId: String,
    val pixList: List<ListPixPixResponse>
) {
}