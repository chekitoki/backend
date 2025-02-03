package com.example.chekitoki.utils

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

object RequestUtils {
    fun getHttpServletRequest(): HttpServletRequest {
        return (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
    }

    fun getHttpServletResponse(): HttpServletResponse {
        return (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).response
            ?: throw IllegalStateException("HttpServletResponse is not available.")
    }
}