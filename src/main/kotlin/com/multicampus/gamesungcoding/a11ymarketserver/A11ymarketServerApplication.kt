package com.multicampus.gamesungcoding.a11ymarketserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["com.multicampus.gamesungcoding.a11ymarketserver.common.properties"])
class A11ymarketServerApplication

fun main(args: Array<String>) {
    runApplication<A11ymarketServerApplication>(*args)
}
