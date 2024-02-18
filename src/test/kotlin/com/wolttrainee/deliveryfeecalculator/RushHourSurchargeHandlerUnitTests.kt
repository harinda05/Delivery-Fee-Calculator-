package com.wolttrainee.deliveryfeecalculator

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderFactory
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst
import com.wolttrainee.deliveryfeecalculator.service.handlers.RushHourSurchargeHandler
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTest
class RushHourSurchargeHandlerUnitTests {
    lateinit var rushHourSurchargeHandler: RushHourSurchargeHandler

    @Autowired
    val externalConfigReaderFactory = ExternalConfigReaderFactory()
    var currentFee = 0

    @BeforeEach
    fun setup() {
        rushHourSurchargeHandler = RushHourSurchargeHandler(externalConfigReaderFactory.configReader())
        currentFee = 100
    }

//    During the Friday rush, 3 - 7 PM, the delivery fee (the total fee including possible surcharges) will be multiplied by 1.2x. However, the fee still cannot be more than the max (15€).
//    Considering timezone, for simplicity, use UTC as a timezone in backend solutions (so Friday rush is 3 - 7 PM UTC).

    @Test
    fun `delivery time is 2 59m 59s PM on a Friday, current fee would not be multiplied`(){
        val utcTimeString = "2024-01-26T14:59:59Z"
        val localFee = rushHourSurchargeHandler.calculateFee(DeliveryFeeRequestModel(0, 0,0, Instant.parse(utcTimeString)), currentFee)
        Assertions.assertEquals(currentFee, localFee)
    }

    @Test
    fun `delivery time is 3 PM on a Friday, current fee would multiplied one point two`(){
        val utcTimeString = "2024-01-26T15:00:00Z"
        val localFee = rushHourSurchargeHandler.calculateFee(DeliveryFeeRequestModel(0, 0,0, Instant.parse(utcTimeString)), currentFee)
        Assertions.assertEquals((currentFee * 1.2).toInt(), localFee)
    }

    @Test
    fun `delivery time is 4 PM on a Friday, current fee would multiplied one point two`(){
        val utcTimeString = "2024-01-26T16:00:00Z"
        val localFee = rushHourSurchargeHandler.calculateFee(DeliveryFeeRequestModel(0, 0,0, Instant.parse(utcTimeString)), currentFee)
        Assertions.assertEquals((currentFee * 1.2).toInt(), localFee)
    }

    @Test
    fun `delivery time is 7 PM on a Friday, current fee would multiplied one point two`(){
        val utcTimeString = "2024-01-26T19:00:00Z"
        val localFee = rushHourSurchargeHandler.calculateFee(DeliveryFeeRequestModel(0, 0,0, Instant.parse(utcTimeString)), currentFee)
        Assertions.assertEquals((currentFee * 1.2).toInt(), localFee)
    }

    @Test
    fun `delivery time is 7 00m 01s PM on a Friday, current fee would not be multiplied`(){
        val utcTimeString = "2024-01-26T19:00:01Z"
        val localFee = rushHourSurchargeHandler.calculateFee(DeliveryFeeRequestModel(0, 0,0, Instant.parse(utcTimeString)), currentFee)
        Assertions.assertEquals(currentFee, localFee)
    }

    @Test
    fun `delivery time is 4 PM on a Saturday, current fee would multiplied one point two`(){
        val utcTimeString = "2024-01-27T16:00:00Z"
        val localFee = rushHourSurchargeHandler.calculateFee(DeliveryFeeRequestModel(0, 0,0, Instant.parse(utcTimeString)), currentFee)
        Assertions.assertEquals(currentFee, localFee)
    }

    @Test
    fun `currentFee is 14 eur, delivery time is 4 PM on a Friday, current fee would multiplied one point two, max fee is returned`(){
        currentFee = 1400
        val utcTimeString = "2024-01-26T16:00:00Z"
        val localFee = rushHourSurchargeHandler.calculateFee(DeliveryFeeRequestModel(0, 0,0, Instant.parse(utcTimeString)), currentFee)
        Assertions.assertEquals(externalConfigReaderFactory.configReader().getByKey(FeeComponentConst.MAX_FEE_CONSTRAINT).toInt(), localFee)
    }
}