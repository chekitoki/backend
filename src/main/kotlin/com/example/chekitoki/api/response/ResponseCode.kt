package com.example.chekitoki.api.response

enum class ResponseCode(
    val code: Int,
    val message: String,
) {
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "bad request"),
    UNAUTHORIZED(401, "unauthorized"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "not found"),
    METHOD_NOT_SUPPORTED(405, "method not supported"),
    DUPLICATE(409, "duplicate"),
    SERVER_ERROR(500, "server error"),
    NOT_IMPLEMENTED(501, "not implemented"),
}