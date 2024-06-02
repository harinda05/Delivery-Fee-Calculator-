package com.wolttrainee.deliveryfeecalculator.model

import com.wolttrainee.deliveryfeecalculator.dto.ErrorDetail
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class DeliveryFeeRequestModel(
    val cart_value: CartValue,
    val delivery_distance: Int,
    val number_of_items: Int,
    val time: Instant
)

@JvmInline
value class CartValue(private val cart_value: Int) {
    init {
        require(validateCartValue(cart_value)) {
            Json.encodeToString(ErrorDetail(field = "cart_value",  "Invalid JSON payload: cart_value: $cart_value is invalid"))
        }
    }

    fun getValue(): Int {
        return cart_value;
    }

    private companion object {
        fun validateCartValue(cart_value: Int): Boolean {

            if (cart_value >= 0) {
                return true
            }
            return false
        }
    }
}