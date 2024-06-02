package com.wolttrainee.deliveryfeecalculator.controller

import com.wolttrainee.deliveryfeecalculator.dto.DeliveryFeeRequest
import com.wolttrainee.deliveryfeecalculator.dto.DeliveryFeeResponse
import com.wolttrainee.deliveryfeecalculator.service.DeliveryFeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/fees")
class DeliveryFeeController(private val deliveryFeeService: DeliveryFeeService) {

    @PostMapping("/delivery-fee")
    fun getDeliveryFee(@RequestBody deliveryFeeRequest: DeliveryFeeRequest): ResponseEntity<Any> {
        val fee = deliveryFeeService.getDeliveryFee(deliveryFeeRequest)
        return ResponseEntity.status(HttpStatus.OK).body(DeliveryFeeResponse(fee))
    }
}