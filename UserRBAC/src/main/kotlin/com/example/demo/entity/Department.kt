package com.example.demo.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "departments") // Explicitly set table name
data class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()") // Ensure default UUID generation
    var id: UUID? = null,

    @Column(unique = true, nullable = false) // Ensure department names are unique
    val name: String,

    @ManyToMany(mappedBy = "departments", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    val users: MutableSet<User> = mutableSetOf()
)
