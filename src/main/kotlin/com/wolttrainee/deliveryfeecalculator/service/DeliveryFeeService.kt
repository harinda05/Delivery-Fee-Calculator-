package com.wolttrainee.deliveryfeecalculator.service

import com.wolttrainee.deliveryfeecalculator.dto.DeliveryFeeRequest
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.handlers.FeeHandler
import kotlinx.datetime.Instant
import org.springframework.stereotype.Service

@Service
class DeliveryFeeService(private val feeHandler: FeeHandler) {
    fun getDeliveryFee(deliveryFeeRequest: DeliveryFeeRequest): Int {
        val deliveryFeeRequestModel = DeliveryFeeRequestModel(deliveryFeeRequest.cart_value,
            deliveryFeeRequest.delivery_distance,
            deliveryFeeRequest.number_of_items,
            Instant.parse(deliveryFeeRequest.time))

        return feeHandler.calculateFee(deliveryFeeRequestModel, 0)
    }
}