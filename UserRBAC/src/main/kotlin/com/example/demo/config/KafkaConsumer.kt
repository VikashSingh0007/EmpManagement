package com.example.demo.config

import com.example.demo.entity.User
import com.example.demo.entity.Department
import com.example.demo.repository.UserRepository
import com.example.demo.repository.DepartmentRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.*

@Service
class KafkaConsumer(
    private val userRepository: UserRepository,
    private val departmentRepository: DepartmentRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(KafkaConsumer::class.java)

    @KafkaListener(topics = ["user-createdFromNestedService"], groupId = "kotlin-consumer-group")
    fun consumeUserCreatedMessage(message: String) {
        logger.info("Received user creation message: $message")
        try {
            val parsedMessage = ObjectMapper().readTree(message)
            logger.info("Parsed user creation message: $parsedMessage")

            // ✅ Extract ID from Kafka message (String instead of UUID)
            val id = parsedMessage.get("userId")?.asText() ?: UUID.randomUUID().toString() // Use default if missing
            val password = parsedMessage.get("password").asText()
            val name = parsedMessage.get("name").asText()
            val email = parsedMessage.get("email").asText()
            val profilePicture = parsedMessage.get("profilePicture")?.asText()
            val role = parsedMessage.get("role").asText()

            // ✅ Check if user already exists in the Kotlin DB
            val existingUser = userRepository.findByEmail(email)
            if (existingUser.isPresent) {
                logger.info("User with email $email already exists. Skipping creation.")
                return
            }

            // ✅ Create user with the ID from NestJS (or default if missing)
            val user = User(
                id = id, // Now using String
                name = name,
                password = password,
                email = email,
                profilePicture = profilePicture,
                role = role
            )
            println(user)
            userRepository.save(user)
            logger.info("✅ User saved to the database: $user")

        } catch (e: Exception) {
            logger.error("❌ Error parsing or saving user message: ${e.message}", e)
        }
    }

    @KafkaListener(topics = ["department-created"], groupId = "kotlin-consumer-group")
    fun consumeDepartmentCreatedMessage(message: String) {
        logger.info("Received department creation message: $message")
        try {
            val parsedMessage = ObjectMapper().readTree(message)
            logger.info("Parsed department creation message: $parsedMessage")

            val departmentName = parsedMessage.get("name").asText()

            val newDepartment = Department(name = departmentName)
            departmentRepository.save(newDepartment)
            logger.info("✅ New department created and saved to the database: $newDepartment")

        } catch (e: Exception) {
            logger.error("❌ Error parsing or saving department message: ${e.message}", e)
        }
    }
}
