package com.wolttrainee.deliveryfeecalculator.service.handlers
import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderInterface
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.MAX_FEE_CONSTRAINT
import org.springframework.stereotype.Component
import kotlin.math.max

@Component
abstract class FeeHandler (protected val configReader: ExternalConfigReaderInterface) {

    abstract fun calculateFee(deliveryFeeRequestModel: DeliveryFeeRequestModel, currentFee: Int): Int
    abstract var nextHandler: FeeHandler?

    protected var maxFeeConstraint = configReader.getByKey(MAX_FEE_CONSTRAINT).toInt();

    protected fun  exceedsMaxFeeConstraint(currentFee: Int): Boolean {
        return currentFee > maxFeeConstraint
    }
}