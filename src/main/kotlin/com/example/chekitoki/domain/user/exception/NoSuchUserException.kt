package com.example.chekitoki.domain.user.exception

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode

class NoSuchUserException(
    message: String?,
): BusinessException(
    ResponseCode.NOT_FOUND,
    message,
) {
}