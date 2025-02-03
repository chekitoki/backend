package com.example.chekitoki.config.auth.jwt.exception

import com.example.chekitoki.api.response.ApiResponse
import com.example.chekitoki.api.response.ResponseCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class JwtAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        val apiResponse: ApiResponse<*> = ApiResponse.exceptionError(ResponseCode.FORBIDDEN, accessDeniedException?.localizedMessage ?: "Forbidden")

        response?.status = HttpServletResponse.SC_FORBIDDEN
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        response?.writer?.write(objectMapper.writeValueAsString(apiResponse))
    }
}