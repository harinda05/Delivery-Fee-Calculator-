package com.wolttrainee.deliveryfeecalculator.configurations

import org.springframework.stereotype.Component

@Component
interface ExternalConfigReaderInterface {
    fun getByKey(key: String): String
}