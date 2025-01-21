package com.example.chekitoki.api.exception

import com.example.chekitoki.api.response.ResponseCode

open class BusinessException(
    val code: ResponseCode,
    message: String?
) : RuntimeException(message ?: code.message) {
}