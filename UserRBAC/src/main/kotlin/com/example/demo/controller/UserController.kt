package com.example.demo.controller

import com.example.demo.dto.CreateUserDto
import com.example.demo.dto.LoginDto
import com.example.demo.dto.UpdateUserDto
import com.example.demo.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody createUserDto: CreateUserDto) = userService.createUser(createUserDto)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updateUserDto: UpdateUserDto) =
        userService.updateUser(id, updateUserDto)

    @GetMapping("/all")
    fun getAllUsers() = userService.getAllUsers()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: Long) = userService.deleteUser(id)


    //yha dal rha hu
    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<Map<String, String>> {
        println("chal rha h ")
        println(loginDto)

        // Generate token
        val token = userService.loginUser(loginDto)

        // Return the token along with a message indicating it's a JWT token
        val response = mapOf("message" to "JWT Token", "token" to token)
        return ResponseEntity.ok(response)
    }


}
