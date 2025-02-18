package com.example.demo.dto

data class UpdateUserDto(
    val email: String?,
    val password: String?,
    val name: String?,
    val profilePicture: String?
)
