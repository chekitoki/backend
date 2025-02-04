package com.example.chekitoki.domain.user.exception

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode

class DuplicatePasswordException(
    message: String?,
) : BusinessException(
    ResponseCode.BAD_REQUEST,
    message,
) {
}