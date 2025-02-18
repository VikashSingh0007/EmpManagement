package com.example.demo.dto


data class CreateUserDto(
    val email: String,
    val password: String,
    val role: String? = "USER", // Default role is USER
    val name: String? = null,
    val profilePicture: String? = null
)
