package com.example.demo.repository

import com.example.demo.entity.Department
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : JpaRepository<Department, Long>
