package com.wolttrainee.deliveryfeecalculator.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class ExternalConfigReaderFactory() {

    private val json: String = "json"
    private val sqlDB: String = "sqlDB" // implementation needed
    private val mongoDB: String = "mongoDB" // implementation needed
    @Value("\${application.external.config.source}")
    private lateinit var configSource: String

    fun configReader(): ExternalConfigReaderInterface{
        lateinit var externalConfigReaderAbstract: ExternalConfigReaderInterface;

        if(configSource == json){
            externalConfigReaderAbstract = JsonConfigReader()
        }
        // else ability to extend config source as required in the future.
        return externalConfigReaderAbstract;
    }

}