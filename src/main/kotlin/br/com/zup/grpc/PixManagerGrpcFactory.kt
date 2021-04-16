package br.com.zup.grpc

import com.zup.br.orange.*
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class PixManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registerPix() = PixKeyRegisterServiceGrpc.newBlockingStub(channel);

    @Singleton
    fun deletePix() = PixKeyDeleteServiceGrpc.newBlockingStub(channel);

    @Singleton
    fun consultPix() = PixKeyConsultServiceGrpc.newBlockingStub(channel);

    @Singleton
    fun listPix() = PixKeyListServiceGrpc.newBlockingStub(channel);
}