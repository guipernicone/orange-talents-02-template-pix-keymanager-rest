package br.com.zup.validation

import br.com.zup.controller.request.RegisterPixRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixValidator::class])
annotation class ValidPix(
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
    val massage: String = "Invalid Pix Key"
)

@Singleton
class ValidPixValidator() : ConstraintValidator<ValidPix, RegisterPixRequest> {

    override fun isValid(
        @Nullable value: RegisterPixRequest?,
        @NonNull annotationMetadata: AnnotationValue<ValidPix>?,
        @NonNull context: ConstraintValidatorContext?
    ): Boolean {

        if(value?.keyType == null)
            return false

        return value.keyType.valid(value.keyValue)
    }
}