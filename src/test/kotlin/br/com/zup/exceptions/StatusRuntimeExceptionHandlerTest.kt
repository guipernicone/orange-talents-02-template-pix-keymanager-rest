package br.com.zup.exceptions

import br.com.zup.exception.StatusRuntimeExceptionHandler
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpRequest
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class StatusRuntimeExceptionHandlerTest {
    private val genericRequest: MutableHttpRequest<Any> = HttpRequest.GET<Any>("/")

    @Test
    fun testHandleNotFoundException(){
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription("not found"))

        val response = StatusRuntimeExceptionHandler().handle(genericRequest, notFoundException)

        with(response) {
            Assertions.assertEquals(HttpStatus.NOT_FOUND, this.status())
            Assertions.assertEquals("NOT_FOUND: not found", this.body())
        }
    }

    @Test
    fun testHandleInvalidArgument(){
        val notFoundException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("invalid argument"))

        val response = StatusRuntimeExceptionHandler().handle(genericRequest, notFoundException)

        with(response) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, this.status())
            Assertions.assertEquals("INVALID_ARGUMENT: invalid argument", this.body())
        }
    }

    @Test
    fun testHandleAlreadyExists(){
        val notFoundException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription("already exists"))

        val response = StatusRuntimeExceptionHandler().handle(genericRequest, notFoundException)

        with(response) {
            Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.status())
            Assertions.assertEquals("ALREADY_EXISTS: already exists", this.body())
        }
    }

    @Test
    fun testHandleInternalError(){
        val notFoundException = StatusRuntimeException(Status.INTERNAL.withDescription("Internal Error"))

        val response = StatusRuntimeExceptionHandler().handle(genericRequest, notFoundException)

        with(response) {
            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.status())
            Assertions.assertEquals("INTERNAL: Internal Error", this.body())
        }
    }

    @Test
    fun testHandleDefault(){
        val notFoundException = StatusRuntimeException(Status.ABORTED.withDescription("aborted"))

        val response = StatusRuntimeExceptionHandler().handle(genericRequest, notFoundException)

        with(response) {
            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.status())
            Assertions.assertEquals("It was not possible to complete the request", this.body())
        }
    }
}