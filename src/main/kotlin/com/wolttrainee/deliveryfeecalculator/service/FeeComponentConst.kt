package com.wolttrainee.deliveryfeecalculator.service

object FeeComponentConst {

    const val MAX_FEE_CONSTRAINT = "maxFee"

    // cart value constants
    const val FREE_DELIVERY_ELIGIBLE_CART_VALUE = "freeDeliveryEligibleCartValue"
    const val MINIMUM_CART_VALUE = "minCartValue"

    // no of items constants
    const val NO_OF_ITEMS_BEFORE_SURCHARGE = "noOfItemsBeforeSurcharge"
    const val SURCHARGE_FOR_EXCEEDING_NO_OF_ITEMS = "surchargeForExceedingNoOfItems"
    const val MAX_NO_OF_ITEMS_BEFORE_BULK_FEE = "maxNoOfItemsBeforeBulkFee"
    const val EXTRA_BULK_FEE = "extraBulkFee"

    // delivery distance constants
    const val STANDARD_FEE_FOR_FIRST_KILO_METER = "standardFeeForFirstKiloMeter"
    const val ADDITIONAL_DISTANCE_INCREMENT = "additionalDistanceIncrement"
    const val FEE_PER_ADDITIONAL_INCREMENT = "feePerAdditionalDistanceIncrement"

    // rush hour constants
    const val RUSH_DAY_OF_WEEK = "rushDayOfWeek" //iso number of the day
    const val RUSH_HOUR_START_TIME = "rushHourStartTime"
    const val RUSH_HOUR_END_TIME = "rushHourEndTime"
    const val RUSH_HOUR_FEE_MULTIPLIER = "rushHourFeeMultiplier"
}