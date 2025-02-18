package com.example.demo.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    private val secretKey = "w8OsW+kXW75fQ1w+EbnUZoo//7lu08zdAeDwm6/kbjw="  // Use a secure secret key in production

    // Generate JWT Token
    fun generateToken(email: String, role: String): String {

        return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    }

    // Extract username (email) from JWT Token

    fun extractUsername(token: String): String {
//        val claims = Jwts.parser()
//            .setSigningKey(secretKey)
//            .parseClaimsJws(token)
//            .body
//
//        println("Extracted Role from JWT: ${claims["role"]}")  // Debugging line

        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .subject
    }

    // Validate if token is expired
    fun isTokenExpired(token: String): Boolean {
        val expiration = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .expiration
        return expiration.before(Date())
    }
    fun extractClaim(token: String, claimKey: String): Any? {
        val claim=Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body[claimKey]  // Extract any claim by key (e.g., "role")
        println(claim)
        return claim
    }
    // Validate JWT Token by checking both email and expiration
    fun validateToken(token: String, email: String): Boolean {
        val username = extractUsername(token)
        return (username == email && !isTokenExpired(token))
    }


}
