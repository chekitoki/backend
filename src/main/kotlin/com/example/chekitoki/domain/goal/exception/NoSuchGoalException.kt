package com.example.chekitoki.domain.goal.exception

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode

class NoSuchGoalException(
    message: String?
) : BusinessException(ResponseCode.NOT_FOUND, message)