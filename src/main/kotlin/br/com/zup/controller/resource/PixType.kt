package br.com.zup.controller.resource

import io.micronaut.validation.validator.constraints.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class PixType {
    CPF{
        override fun valid(key: String?): Boolean {
            return CPFValidator().run {
                initialize(null)
                isValid(key, null)
            }
        }
    },
    PHONE {
        override fun valid(key: String?): Boolean {
            if (key.isNullOrBlank())
                return false

            return key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL {
        override fun valid(key: String?): Boolean {
            if (key.isNullOrBlank()){
                return false
            }
            return EmailValidator().run {
                initialize(null)
                isValid(key,null )
            }
        }
    },
    RANDOM {
        override fun valid(key: String?): Boolean {
            return key.isNullOrBlank()
        }
    };
    abstract fun valid(key: String?): Boolean
}