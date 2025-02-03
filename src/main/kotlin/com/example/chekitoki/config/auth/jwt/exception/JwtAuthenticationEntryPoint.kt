package com.example.chekitoki.config.auth.jwt.exception

import com.example.chekitoki.api.response.ApiResponse
import com.example.chekitoki.api.response.ResponseCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        val apiResponse: ApiResponse<*> = ApiResponse.exceptionError(ResponseCode.BAD_REQUEST, authException?.localizedMessage ?: "Unauthorized")

        response?.status = HttpServletResponse.SC_BAD_REQUEST
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        response?.writer?.write(objectMapper.writeValueAsString(apiResponse))
    }
}