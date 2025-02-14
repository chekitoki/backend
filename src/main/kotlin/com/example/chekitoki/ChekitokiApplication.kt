package com.example.chekitoki

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class ChekitokiApplication

fun main(args: Array<String>) {
    runApplication<ChekitokiApplication>(*args)
}
