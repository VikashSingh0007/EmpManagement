package com.example.demo.service


import com.example.demo.entity.Department
import com.example.demo.repository.DepartmentRepository
import com.example.demo.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val userRepository: UserRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun createDepartment(name: String): Department {
        val department = Department(name = name)
        val savedDepartment = departmentRepository.save(department)
        val departmentMessage = mapOf(
            "id" to savedDepartment.id,
            "name" to savedDepartment.name
        )
        val objectMapper = ObjectMapper()
        val jsonMessage = objectMapper.writeValueAsString(departmentMessage)
        kafkaTemplate.send("user-department-group", jsonMessage)
        return savedDepartment
    }



    fun addUserToDepartment(userId: Long, departmentId: Long): Department {
        val user = userRepository.findById(userId).orElseThrow { Exception("User not found.") }
        val department = departmentRepository.findById(departmentId).orElseThrow { Exception("Department not found.") }

        // Add the user to the department
        department.users.add(user)

        // Save the department (this will also save the user-department relation in the join table)
        departmentRepository.save(department)
        val departmentMessage = mapOf(
            "userId" to user.id,
            "departmentId" to department.id,
            "departmentName" to department.name,
            "userName" to user.name
        )
        val objectMapper = ObjectMapper()
        val jsonMessage = objectMapper.writeValueAsString(departmentMessage)
        kafkaTemplate.send("user-added-to-department", jsonMessage)

        return department
    }


}
