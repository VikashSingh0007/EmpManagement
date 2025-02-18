package com.example.demo.security

import com.example.demo.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Component
class JwtAuthorizationFilter(
    private val jwtUtil: JwtUtil,
    private val userService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val header = request.getHeader("Authorization")

        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)

            try {
                // Extract email from the token
                val email = jwtUtil.extractUsername(token)
                println("from token email:$email")

                // Extract role from the token
                val role = jwtUtil.extractClaim(token, "role") as String
                println("Extracted Role from JWT: $role")

                // Fetch user details from the service
                val userDetails = userService.loadUserByUsername(email)
                println("Extracted user details: $userDetails")

                // Validate the token with the user's email
                if (jwtUtil.validateToken(token, email) ) {
                    // Set up the authentication token
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                    println("ye chl gya matabl authorised h : $authentication")
                }
            } catch (e: Exception) {
                // Handle invalid or expired token
                println("jo bhi ye cchl rha h gdbdi")
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Unauthorized - Invalid or expired JWT token")
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
