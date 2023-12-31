package com.soul.core.web.rest

import com.soul.core.config.RestEndpoints.ADMIN_PATH
import com.soul.core.service.UserService
import com.soul.core.service.dto.CreateUserRequest
import com.soul.core.service.dto.UpdateUserRequest
import com.soul.core.service.dto.UserDto
import com.soul.core.service.mapper.UserMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(ADMIN_PATH)
class UserController(
    private var userService: UserService,
    private var mapper: UserMapper
) {

    @PostMapping
    fun create(@Valid @RequestBody user: CreateUserRequest): ResponseEntity<UserDto> {
        val created = userService.create(user)
        return ResponseEntity.ok(mapper.toDto(created))
    }

    @PutMapping("{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody user: UpdateUserRequest): ResponseEntity<UserDto> {
        val updated = userService.update(id, user)
        return ResponseEntity.ok(mapper.toDto(updated))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("{id}")
    fun get(@PathVariable id: String): ResponseEntity<UserDto> {
        val user = userService.get(id)
        return ResponseEntity.ok(mapper.toDto(user))
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<UserDto>> {
        val users = userService.getAll()
        return ResponseEntity.ok(mapper.toDtoList(users))
    }
}