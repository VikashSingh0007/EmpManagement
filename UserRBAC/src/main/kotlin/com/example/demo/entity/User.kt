package com.example.demo.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users") // Explicitly set table name
data class User(
    @Id
    @Column(nullable = false, unique = true)
    var id: String = UUID.randomUUID().toString(),  // ✅ Default to UUID as String

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var role: String = "USER",  // Default role is USER

    var name: String? = null,

    var profilePicture: String? = null,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinTable(
        name = "user_department",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "department_id")]
    )
    val departments: MutableSet<Department> = mutableSetOf()
)
