package com.example.chekitoki.domain.user.exception

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode

class InvalidCredentialsException(
    message: String?,
) : BusinessException(ResponseCode.UNAUTHORIZED, message)