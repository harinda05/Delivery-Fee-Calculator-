package com.wolttrainee.deliveryfeecalculator.configurations

import kotlinx.serialization.json.*

class  JsonConfigReader : ExternalConfigReaderInterface {
    private val fileContentString = this::class.java.classLoader.getResource("configs.json")?.readText()
    private val jsonObject: JsonObject? = fileContentString?.let { Json.parseToJsonElement(it).jsonObject}

    override fun getByKey(key: String): String {
        return jsonObject?.get(key)?.jsonPrimitive?.contentOrNull ?: throw NoSuchElementException("Key not found: $key")
    }
}