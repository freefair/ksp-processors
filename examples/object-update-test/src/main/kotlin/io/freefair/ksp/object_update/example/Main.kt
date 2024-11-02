package io.freefair.ksp.object_update.example

import io.freefair.ksp.object_update.example.model.UserModel
import io.freefair.ksp.object_update.example.model.update
import java.time.LocalDateTime

fun main() {
    val user = UserModel(
        name = "John Doe",
        age = 23,
        email = "john@example.org",
        enabled = true,
        roles = listOf("USER", "ADMIN"),
        created_at = LocalDateTime.MIN
    )

    val update = UserModel(
        name = "<NAME>",
        age = 24,
        email = "<EMAIL>",
        enabled = false,
        roles = listOf("USER"),
        created_at = LocalDateTime.MAX
    )

    user.update(update)

    println(user)
}