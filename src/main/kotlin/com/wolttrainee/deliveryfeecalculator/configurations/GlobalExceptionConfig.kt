package com.wolttrainee.deliveryfeecalculator.configurations

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.wolttrainee.deliveryfeecalculator.dto.ErrorDetail
import com.wolttrainee.deliveryfeecalculator.dto.ErrorResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class GlobalExceptionConfig{

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTypeMissMatchExceptions(ex: SerializationException): ResponseEntity<ErrorResponse> {

        val message = "Invalid value"
        val field = extractFieldNameFromSerializationException(ex) ?: "unknown"
        val errorDetail = ErrorDetail(field = field, message = message)
        val response = ErrorResponse(
            error = "Bad Request",
            details = errorDetail
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    private fun extractFieldNameFromSerializationException(ex: SerializationException): String? {
        val message = ex.message ?: return null
        val pathPrefix = "path: $"
        val pathIndex = message.indexOf(pathPrefix)
        return if (pathIndex != -1) {
            val startIndex = pathIndex + pathPrefix.length
            val endIndex = message.indexOf('\n', startIndex)
            if (endIndex != -1) {
                message.substring(startIndex + 1, endIndex)
            } else {
                message.substring(startIndex + 1)
            }
        } else {
            null
        }
    }
}