package com.wolttrainee.deliveryfeecalculator

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestDeliveryFeeCalculatorApplication

fun main(args: Array<String>) {
	fromApplication<DeliveryFeeCalculatorApplication>().with(TestDeliveryFeeCalculatorApplication::class).run(*args)
}
