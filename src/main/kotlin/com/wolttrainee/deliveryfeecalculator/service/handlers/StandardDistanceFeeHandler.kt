package com.wolttrainee.deliveryfeecalculator.service.handlers

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderInterface
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst

class StandardDistanceFeeHandler(configReader: ExternalConfigReaderInterface) : FeeHandler(configReader) {
    override fun calculateFee(deliveryFeeRequestModel: DeliveryFeeRequestModel, currentFee: Int): Int {
        var localFee: Int = configReader.getByKey(FeeComponentConst.STANDARD_FEE_FOR_FIRST_KILO_METER).toInt() + currentFee

        // get remaining distance
        val remainingDistance: Int = maxOf(0, deliveryFeeRequestModel.delivery_distance - 1000)

        // Calculate and add the additional fee to base fee
        if (remainingDistance != 0)
            localFee += getRemainingDistanceIncrements(remainingDistance) * configReader.getByKey(FeeComponentConst.FEE_PER_ADDITIONAL_INCREMENT).toInt()

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

    private fun getRemainingDistanceIncrements(remainingDistance: Int): Int {
        val distanceIncrement = configReader.getByKey(FeeComponentConst.ADDITIONAL_DISTANCE_INCREMENT).toInt()

        var increments: Int = remainingDistance / distanceIncrement
        val remainder =  remainingDistance % distanceIncrement

        if(remainder > 0)
            increments += 1
        return increments
    }
}