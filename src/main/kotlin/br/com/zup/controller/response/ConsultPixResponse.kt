package br.com.zup.controller.response

import java.time.LocalDate
import java.time.LocalDateTime

class ConsultPixResponse(
    val clientId: String,
    val pixId: String,
    val pix: ConsultPixPixResponse
)