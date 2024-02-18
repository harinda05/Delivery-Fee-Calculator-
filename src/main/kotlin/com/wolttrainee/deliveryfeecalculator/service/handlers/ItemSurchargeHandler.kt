package com.wolttrainee.deliveryfeecalculator.service.handlers

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderInterface
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.EXTRA_BULK_FEE
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.MAX_NO_OF_ITEMS_BEFORE_BULK_FEE
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.NO_OF_ITEMS_BEFORE_SURCHARGE
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.SURCHARGE_FOR_EXCEEDING_NO_OF_ITEMS

class ItemSurchargeHandler(configReader: ExternalConfigReaderInterface) : FeeHandler(configReader) {
    override fun calculateFee(deliveryFeeRequestModel: DeliveryFeeRequestModel, currentFee: Int): Int {
        var localFee = currentFee
        val noOfItemsBeforeSurchargeAdded: Int = configReader.getByKey(NO_OF_ITEMS_BEFORE_SURCHARGE).toInt()

        // Check for maximum number of items allowed without surcharge
        if (deliveryFeeRequestModel.number_of_items > noOfItemsBeforeSurchargeAdded) {
            val surchargeAmount: Int = configReader.getByKey(SURCHARGE_FOR_EXCEEDING_NO_OF_ITEMS).toInt()
            localFee += surchargeAmount * (deliveryFeeRequestModel.number_of_items - noOfItemsBeforeSurchargeAdded)

            // check for extra bulk fee
            val maxNoOfItemsBeforeBulkFee: Int = configReader.getByKey(MAX_NO_OF_ITEMS_BEFORE_BULK_FEE).toInt()
            if (deliveryFeeRequestModel.number_of_items > maxNoOfItemsBeforeBulkFee)
                localFee += configReader.getByKey(EXTRA_BULK_FEE).toInt()
        }

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