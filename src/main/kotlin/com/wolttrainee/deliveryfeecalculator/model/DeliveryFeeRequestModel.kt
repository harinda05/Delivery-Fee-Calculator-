package com.wolttrainee.deliveryfeecalculator.model

import kotlinx.datetime.Instant

data class DeliveryFeeRequestModel(
    val cart_value: Int,
    val delivery_distance: Int,
    val number_of_items: Int,
    val time: Instant
)
