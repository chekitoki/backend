package com.example.chekitoki.api.exception

import com.example.chekitoki.api.response.ResponseCode

class ResourceAuthorizationException(
    message: String?
) : BusinessException(
    ResponseCode.FORBIDDEN,
    message,
) {
}