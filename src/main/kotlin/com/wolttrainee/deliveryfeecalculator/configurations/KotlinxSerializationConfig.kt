package com.wolttrainee.deliveryfeecalculator.configurations

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

@Configuration
class KotlinxSerializationConfig {
    @Bean
    fun json(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true

    }

    @Bean
    fun kotlinSerializationConverter(json: Json): KotlinSerializationJsonHttpMessageConverter {
        return KotlinSerializationJsonHttpMessageConverter(json)
    }

}
