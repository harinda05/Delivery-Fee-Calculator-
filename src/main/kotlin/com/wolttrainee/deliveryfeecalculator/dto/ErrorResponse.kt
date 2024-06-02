package com.wolttrainee.deliveryfeecalculator.dto

import kotlinx.serialization.Serializable

data class ErrorResponse (
    val error: String,
    val details: ErrorDetail
)

@Serializable
data class ErrorDetail(
    val field: String,
    val message: String
)
