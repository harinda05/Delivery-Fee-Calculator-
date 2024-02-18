package com.wolttrainee.deliveryfeecalculator

import com.wolttrainee.deliveryfeecalculator.service.handlers.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DeliveryFeeCalculatorApplication

fun main(args: Array<String>) {
	runApplication<DeliveryFeeCalculatorApplication>(*args)
}

