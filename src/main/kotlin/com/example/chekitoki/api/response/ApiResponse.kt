package com.example.chekitoki.api.response

import org.springframework.validation.BindingResult

class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?,
) {
    companion object {
        fun exceptionError(code: ResponseCode, message: String): ApiResponse<*> {
            return ApiResponse(code.code, message, null)
        }

        fun fieldError(bindingResult: BindingResult): ApiResponse<HashMap<String, String>> {
            var errors = HashMap<String, String>()

            bindingResult.fieldErrors.forEach { error ->
                errors[error.field] = error.defaultMessage ?: ""
            }

            return ApiResponse(ResponseCode.BAD_REQUEST.code, "Invalid request", errors)
        }
    }
}