package com.example.chekitoki.api.exception

import com.example.chekitoki.api.response.ApiResponse
import com.example.chekitoki.api.response.ResponseCode
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionManager {
    @ExceptionHandler
    fun handleRunTimeException (exception: RuntimeException): ApiResponse<*> {
        return ApiResponse.exceptionError(ResponseCode.BAD_REQUEST, exception.message ?: "")
    }

    @ExceptionHandler
    fun handleBusinessException (exception: BusinessException): ApiResponse<*> {
        return ApiResponse.exceptionError(exception.code, exception.message ?: exception.code.message)
    }

    @ExceptionHandler
    fun handleArgumentException (exception: MethodArgumentNotValidException): ApiResponse<*> {
        return ApiResponse.fieldError(exception.bindingResult)
    }
}