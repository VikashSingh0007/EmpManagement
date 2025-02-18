package com.example.demo.service

import com.example.demo.dto.CreateUserDto
import com.example.demo.dto.LoginDto
import com.example.demo.dto.UpdateUserDto
import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import com.example.demo.security.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.transaction.Transactional
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository,
                  private val jwtUtil: JwtUtil,
                  private val kafkaTemplate: KafkaTemplate<String, String>
) {

    private val passwordEncoder = BCryptPasswordEncoder()

    @Transactional
    fun createUser(createUserDto: CreateUserDto): User {
        // Step 1: Check if user already exists (optional, if you want to avoid duplicates)
        userRepository.findByEmail(createUserDto.email).orElse(null)?.let {
            throw IllegalArgumentException("User with this email already exists.")
        }


        val encodedPassword = passwordEncoder.encode(createUserDto.password)


        val user = User(
            email = createUserDto.email,
            password = encodedPassword,
            role = createUserDto.role ?: "USER",  // Default role if none is provided
            name = createUserDto.name,
            profilePicture = createUserDto.profilePicture
        )
        val savedUser = userRepository.save(user)
        val userMessage = mapOf(
            "userId" to savedUser.id,
            "email" to savedUser.email,
            "name" to savedUser.name,
            "role" to savedUser.role,
            "profilePicture" to savedUser.profilePicture,
            "password" to savedUser.password
        )
        val objectMapper = ObjectMapper()
        val jsonMessage = objectMapper.writeValueAsString(userMessage)
        kafkaTemplate.send("user-created", jsonMessage)
        println(jsonMessage)
        return savedUser
    }


    fun updateUser(id: Long, updateUserDto: UpdateUserDto): User {
        val user = userRepository.findById(id).orElseThrow { Exception("User not found.") }
        updateUserDto.email?.let { user.email = it }
        updateUserDto.password?.let { user.password = passwordEncoder.encode(it) }
        updateUserDto.name?.let { user.name = it }
        updateUserDto.profilePicture?.let { user.profilePicture = it }
        return userRepository.save(user)
    }

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun deleteUser(id: Long) {
        val user = userRepository.findById(id).orElseThrow { Exception("User not found.") }
        userRepository.delete(user)
    }

    fun loadUserByUsername(username: String): UserDetails {
        println("loaduser chala")
        val user = userRepository.findByEmail(username).orElseThrow {
            UsernameNotFoundException("User not found with email: $username")
        }
        // Convert role to GrantedAuthority
        val authorities = listOf(SimpleGrantedAuthority(user.role))
        println("authrorites chla")
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            authorities
        )
    }

    //ab banega login
    fun loginUser(loginDto: LoginDto): String {
        val user = userRepository.findByEmail(loginDto.email)
            .orElseThrow { BadCredentialsException("Invalid email or password") }

        if (!passwordEncoder.matches(loginDto.password, user.password)) {
            throw BadCredentialsException("Invalid email or password")
        }

        return jwtUtil.generateToken(user.email, user.role)
    }



}
