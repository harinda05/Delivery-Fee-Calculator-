package com.wolttrainee.deliveryfeecalculator.service.handlers

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderInterface
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.FREE_DELIVERY_ELIGIBLE_CART_VALUE
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.MINIMUM_CART_VALUE

class CartValueFeeHandler(configReader: ExternalConfigReaderInterface) : FeeHandler(configReader) {

    override fun calculateFee(deliveryFeeRequestModel: DeliveryFeeRequestModel, currentFee: Int): Int {
        var localFee = currentFee
        val cartValue: Int = deliveryFeeRequestModel.cart_value.getValue()
        val minCartValue = configReader.getByKey(MINIMUM_CART_VALUE).toInt()
        if ( cartValue >= this.configReader.getByKey(FREE_DELIVERY_ELIGIBLE_CART_VALUE).toInt())
            return 0
        else if (cartValue < minCartValue)
            localFee = currentFee + (minCartValue - cartValue)

        return if(exceedsMaxFeeConstraint(localFee)){
            maxFeeConstraint
        } else {
            nextHandler?.calculateFee(deliveryFeeRequestModel, localFee) ?: localFee;
        }
    }

    override var nextHandler: FeeHandler? = null
        set(value) {
            field = value
        }
}