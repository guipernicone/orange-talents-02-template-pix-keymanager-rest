package br.com.zup.utils

import org.mockito.Mockito

class MockitoHelper {
    companion object{
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> uninitialized(): T =  null as T
    }
}