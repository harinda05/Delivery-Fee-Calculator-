package com.wolttrainee.deliveryfeecalculator.dto

data class DeliveryFeeRequest(
    val cart_value: Int,
    val delivery_distance: Int,
    val number_of_items: Int,
    val time: String
)
