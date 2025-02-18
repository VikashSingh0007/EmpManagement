package com.example.demo

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Base64

fun main() {
    val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    val encodedKey = Base64.getEncoder().encodeToString(key.encoded)
    println("Generated Secret Key: $encodedKey")
}
