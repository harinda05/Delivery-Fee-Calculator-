package com.wolttrainee.deliveryfeecalculator.configurations

import com.wolttrainee.deliveryfeecalculator.service.handlers.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChainConfig(@Autowired val externalConfigReaderFactory: ExternalConfigReaderFactory) {
    @Bean
    fun getFeeChainEntry(): FeeHandler  {
        val configReader: ExternalConfigReaderInterface = externalConfigReaderFactory.configReader()

        val firstHandler: FeeHandler  = CartValueFeeHandler(configReader)
        val secondHandler: FeeHandler = ItemSurchargeHandler(configReader)
        val thirdHandler: FeeHandler = StandardDistanceFeeHandler(configReader)
        val fourthHandler: FeeHandler = RushHourSurchargeHandler(configReader)

        firstHandler.nextHandler = secondHandler
        secondHandler.nextHandler = thirdHandler
        thirdHandler.nextHandler = fourthHandler
        fourthHandler.nextHandler = null

        return firstHandler
    }
}
