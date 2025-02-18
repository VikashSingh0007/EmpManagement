package com.example.demo.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedException(request: HttpServletRequest, ex: UnauthorizedException): ResponseEntity<String> {
        return ResponseEntity("Unauthorized: ${ex.message}", HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleForbiddenException(request: HttpServletRequest, ex: ForbiddenException): ResponseEntity<String> {
        return ResponseEntity("Forbidden: ${ex.message}", HttpStatus.FORBIDDEN)
    }


}
