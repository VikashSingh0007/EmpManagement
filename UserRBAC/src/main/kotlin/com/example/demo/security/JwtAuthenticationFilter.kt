package com.example.demo.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {


    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            if (jwtUtil.isTokenExpired(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired")
                return
            }
            val username = jwtUtil.extractUsername(token)
            val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList()) // Add authorities as needed
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }
}
