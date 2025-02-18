package com.example.demo.controller

import com.example.demo.service.DepartmentService
import jakarta.transaction.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/departments")
class DepartmentController(private val departmentService: DepartmentService) {

    @PostMapping
    fun createDepartment(@RequestParam name: String) = departmentService.createDepartment(name)
    @Transactional
    @PostMapping("/{departmentId}/user/{userId}")
    fun addUserToDepartment(@PathVariable userId: Long, @PathVariable departmentId: Long) =
        departmentService.addUserToDepartment(userId, departmentId)

}
