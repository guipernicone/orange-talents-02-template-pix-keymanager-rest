package br.com.zup.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class StatusRuntimeExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<Any> {
        val statusCode = exception.status.code
        val statusDescription = exception.status.description
        val (httpStatus, message) = when (statusCode) {
            Status.NOT_FOUND.code -> Pair (HttpStatus.NOT_FOUND, "$statusCode: $statusDescription")
            Status.INVALID_ARGUMENT.code -> Pair(HttpStatus.BAD_REQUEST, "$statusCode: $statusDescription")
            Status.ALREADY_EXISTS.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, "$statusCode: $statusDescription")
            Status.INTERNAL.code -> Pair(HttpStatus.INTERNAL_SERVER_ERROR, "$statusCode: $statusDescription")
            else -> {
                logger.error("An unexpected error ${exception.javaClass.name} occurred while processing request")
                Pair(HttpStatus.INTERNAL_SERVER_ERROR, "It was not possible to complete the request")
            }
        }

        return HttpResponse.status<Any>(httpStatus).body(message)
    }
}