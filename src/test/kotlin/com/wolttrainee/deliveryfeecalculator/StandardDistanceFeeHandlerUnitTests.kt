package com.wolttrainee.deliveryfeecalculator

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderFactory
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst
import com.wolttrainee.deliveryfeecalculator.service.handlers.StandardDistanceFeeHandler
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StandardDistanceFeeHandlerUnitTests {

    lateinit var standardDistanceFeeHandler: StandardDistanceFeeHandler

    @Autowired
    val externalConfigReaderFactory = ExternalConfigReaderFactory()

    var currentFee = 0

    @BeforeEach
    fun setup() {
        standardDistanceFeeHandler = StandardDistanceFeeHandler(externalConfigReaderFactory.configReader())
        currentFee = 100
    }


//    A delivery fee for the first 1000 meters (=1km) is 2€. If the delivery distance is longer than that, 1€ is added for every additional 500 meters that the courier needs to travel before reaching the destination. Even if the distance would be shorter than 500 meters, the minimum fee is always 1€.


    //    Example 1: If the delivery distance is 1499 meters, the delivery fee surcharge is: 2€ base fee + 1€ for the additional 500 m => 3€
    @Test
    fun `delivery distance is 1499 meters, the local delivery fee is 200 cents base fee + 100 cents for the additional 499 m 300 cents`() {
        val localFee = standardDistanceFeeHandler.calculateFee(DeliveryFeeRequestModel(0, 1499,0, Clock.System.now()), currentFee)
        Assertions.assertEquals((currentFee + 300), localFee)
    }

    //    Example 2: If the delivery distance is 1500 meters, the delivery fee surcharge is: 2€ base fee + 1€ for the additional 500 m => 3€
    @Test
    fun `delivery distance is 1500 meters, the local delivery fee is 200 cents base fee + 100 cents for the additional 500 m 300 cents`() {
        val localFee = standardDistanceFeeHandler.calculateFee(DeliveryFeeRequestModel(0, 1500,0, Clock.System.now()), currentFee)
        Assertions.assertEquals((currentFee + 300), localFee)
    }


//    Example 3: If the delivery distance is 1501 meters, the delivery fee surcharge is: 2€ base fee + 1€ for the first 500 m + 1€ for the second 500 m => 4€

    @Test
    fun `delivery distance is 1501 meters, the local delivery fee  is 200 cents base fee + 200 cents for the additional 501 m`() {
        val localFee = standardDistanceFeeHandler.calculateFee(DeliveryFeeRequestModel(0, 1501,0, Clock.System.now()), currentFee)
        Assertions.assertEquals((currentFee + 400), localFee)
    }

    @Test
    fun `currentFee = 12 eur, delivery distance is 1501 meters, the local delivery fee  is (200 cents base fee) + (200 cents for the additional 501 m) - max fee is returned `() {
        currentFee = 1200
        val localFee = standardDistanceFeeHandler.calculateFee(DeliveryFeeRequestModel(0, 1501,0, Clock.System.now()), currentFee)
        Assertions.assertEquals(externalConfigReaderFactory.configReader().getByKey(FeeComponentConst.MAX_FEE_CONSTRAINT).toInt(), localFee)
    }
}