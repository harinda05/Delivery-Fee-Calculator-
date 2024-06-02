package com.wolttrainee.deliveryfeecalculator.dto

import com.wolttrainee.deliveryfeecalculator.model.CartValue
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

//{"cart_value": 1000, "delivery_distance": 1000, "number_of_iems": 4, "time": "2024-01-27T15:00:00Z"}

@Serializable
data class DeliveryFeeRequest(
    @NotNull
    val cart_value: Int,
    val delivery_distance: Int,
    val number_of_items: Int,
    val time: String
)