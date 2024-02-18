package com.wolttrainee.deliveryfeecalculator

import com.wolttrainee.deliveryfeecalculator.configurations.ExternalConfigReaderFactory
import com.wolttrainee.deliveryfeecalculator.model.DeliveryFeeRequestModel
import com.wolttrainee.deliveryfeecalculator.service.handlers.CartValueFeeHandler
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CartValueFeeHandlerUnitTests {

    lateinit var cartValueFeeHandler: CartValueFeeHandler

    @Autowired
    val externalConfigReaderFactory = ExternalConfigReaderFactory()

    @BeforeEach
    fun setup(){
        cartValueFeeHandler = CartValueFeeHandler(externalConfigReaderFactory.configReader());
    }

    val currentFee = 100


    @Test
    fun `cart value equal 200,  should return 0`(){
        val localFee = cartValueFeeHandler.calculateFee(DeliveryFeeRequestModel(20000, 0,0, Clock.System.now()), currentFee)
        Assertions.assertEquals(0, localFee)
    }

    @Test
    fun `cart value above 200,  should return 0`(){
        val localFee = cartValueFeeHandler.calculateFee(DeliveryFeeRequestModel(30000, 0,0, Clock.System.now()), currentFee)
        Assertions.assertEquals(0, 0)
    }

    @Test
    fun `cart value below 200 and above 10,  should return initial totalFee`(){
        val localFee = cartValueFeeHandler.calculateFee(DeliveryFeeRequestModel(5000, 0,0, Clock.System.now()), currentFee)
        Assertions.assertEquals(currentFee, localFee)
    }

    @Test
    fun `cart value less than 1000 cents (800),  should return currentFee + surcharge(1000-800) cents`(){
        val localFee = cartValueFeeHandler.calculateFee(DeliveryFeeRequestModel(800, 0,0, Clock.System.now()), currentFee)
        Assertions.assertEquals((currentFee + 200), localFee)
    }

}