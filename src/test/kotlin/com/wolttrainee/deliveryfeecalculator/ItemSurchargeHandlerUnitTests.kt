package com.wolttrainee.deliveryfeecalculator

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderFactory
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.FeeComponentConst
import com.wolttrainee.deliveryfeecalculator.service.handlers.ItemSurchargeHandler
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class ItemSurchargeHandlerUnitTests {

    lateinit var itemSurchargeHandler: ItemSurchargeHandler

    @Autowired
    val externalConfigReaderFactory = ExternalConfigReaderFactory()

    var currentFee: Int = 0;
    @BeforeEach
    fun setup(){
        itemSurchargeHandler = ItemSurchargeHandler(externalConfigReaderFactory.configReader())
        currentFee = 100

    }

    @Test
    fun `number of items is 4, no extra surcharge`(){
        val localFee = itemSurchargeHandler.calculateFee(DeliveryFeeRequestModel(10000, 0,4, Clock.System.now()), currentFee)
        Assertions.assertEquals(currentFee, localFee)
    }

    @Test
    fun `number of items is 5, 50 cents surcharge is added`(){
        val localFee = itemSurchargeHandler.calculateFee(DeliveryFeeRequestModel(10000, 0,5, Clock.System.now()), currentFee)
        Assertions.assertEquals(currentFee + 50, localFee)
    }

    @Test
    fun `number of items is 10, 3 euros (300 cents) surcharge (6 x 50 cents) is added`(){
        val localFee = itemSurchargeHandler.calculateFee(DeliveryFeeRequestModel(10000, 0,10, Clock.System.now()), currentFee)
        Assertions.assertEquals(currentFee + 300, localFee)
    }

    @Test
    fun `number of items is 13, 5,70 euros (570 cents) surcharge is added ((9 x 50 cents) + 1,20euros`(){
        val localFee = itemSurchargeHandler.calculateFee(DeliveryFeeRequestModel(10000, 0,13, Clock.System.now()), currentFee)
        Assertions.assertEquals(currentFee + 570, localFee)
    }

    @Test
    fun `current fee is 10 eur , number of items is 13, 5,70 euros (570 cents), max fee is returned `(){
        currentFee = 1000
        val localFee = itemSurchargeHandler.calculateFee(DeliveryFeeRequestModel(10000, 0,13, Clock.System.now()), currentFee)
        Assertions.assertEquals(externalConfigReaderFactory.configReader().getByKey(FeeComponentConst.MAX_FEE_CONSTRAINT).toInt(), localFee)
    }
}