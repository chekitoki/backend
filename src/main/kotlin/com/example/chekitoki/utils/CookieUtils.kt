package com.example.chekitoki.utils

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode
import jakarta.servlet.http.Cookie

object CookieUtils {
    class NoSuchCookieException(message: String?) : BusinessException(ResponseCode.NOT_FOUND, message)

    fun findCookie(name: String): Cookie? {
        val request = RequestUtils.getHttpServletRequest()
        val cookies = request.cookies
        return cookies?.find { it.name == name }
    }

    fun getCookie(name: String): Cookie {
        val request = RequestUtils.getHttpServletRequest()
        val cookies = request.cookies
        return cookies?.find { it.name == name }
            ?: throw NoSuchCookieException("No cookie found. name = $name")
    }

    fun addCookie(name: String, value: String, maxAge: Int) {
        val response = RequestUtils.getHttpServletResponse()
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    fun deleteCookie(cookie: Cookie) {
        val response = RequestUtils.getHttpServletResponse()
        cookie.value = ""
        cookie.path = "/"
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}