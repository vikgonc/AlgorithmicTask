package com.petprijects.algorithm.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun bodyValidFailedResponse(): ResponseEntity<String> {
        return ResponseEntity
            .status(451)
            .body(null)
    }

    @ExceptionHandler(NumberFormatException::class)
    fun convertStringFailedResponse(): ResponseEntity<String> {
        return ResponseEntity
            .status(452)
            .body(null)
    }
}
