package com.wolttrainee.deliveryfeecalculator.service.handlers

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderInterface
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.RUSH_DAY_OF_WEEK
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.RUSH_HOUR_END_TIME
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.RUSH_HOUR_FEE_MULTIPLIER
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst.RUSH_HOUR_START_TIME
import kotlinx.datetime.*

class RushHourSurchargeHandler(configReader: ExternalConfigReaderInterface) : FeeHandler(configReader) {
    override fun calculateFee(deliveryFeeRequestModel: DeliveryFeeRequestModel, currentFee: Int): Int {
        var localFee = currentFee
        val localDateTime = deliveryFeeRequestModel.time.toLocalDateTime(TimeZone.UTC)
        val rushDayOfWeek: DayOfWeek = DayOfWeek(configReader.getByKey(RUSH_DAY_OF_WEEK).toInt()) // Friday = 5: ISO-8601

        if (rushDayOfWeek == localDateTime.dayOfWeek) {
            val localTime = localDateTime.toJavaLocalDateTime().toLocalTime()

            val rushHourStartTimeParts = processTimeString(configReader.getByKey(RUSH_HOUR_START_TIME))
            val rushHourEndTimeParts = processTimeString(configReader.getByKey(RUSH_HOUR_END_TIME))
            val rushHourStartTime = java.time.LocalTime.of(rushHourStartTimeParts[0].toInt(), rushHourStartTimeParts[1].toInt(), rushHourStartTimeParts[2].toInt())
            val rushHourEndTime = java.time.LocalTime.of(rushHourEndTimeParts[0].toInt(), rushHourEndTimeParts[1].toInt(), rushHourEndTimeParts[2].toInt())

            if (localTime.isAfter(rushHourStartTime) && localTime.isBefore(rushHourEndTime)) {
                localFee = localFee.times(configReader.getByKey(RUSH_HOUR_FEE_MULTIPLIER).toFloat()).toInt()
            }
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

    private fun processTimeString(time: String): Array<String>{
        return time.split(":").toTypedArray()
    }
}