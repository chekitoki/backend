package com.example.chekitoki.domain.goalrecord.exception

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.api.response.ResponseCode

class NoSuchGoalRecordException(
    message: String?,
) : BusinessException(ResponseCode.NOT_FOUND, message)