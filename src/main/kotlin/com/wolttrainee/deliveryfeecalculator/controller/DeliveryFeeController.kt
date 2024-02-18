package com.wolttrainee.deliveryfeecalculator.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.wolttrainee.deliveryfeecalculator.dto.DeliveryFeeRequest
import com.wolttrainee.deliveryfeecalculator.dto.DeliveryFeeResponse
import com.wolttrainee.deliveryfeecalculator.service.DeliveryFeeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/fees")
class DeliveryFeeController(private val deliveryFeeService: DeliveryFeeService) {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @PostMapping("/delivery-fee")
    fun getDeliveryFee(@RequestBody deliveryFeeRequest: DeliveryFeeRequest): DeliveryFeeResponse {
        val fee  = deliveryFeeService.getDeliveryFee(deliveryFeeRequest)
        return DeliveryFeeResponse(fee)
    }
}