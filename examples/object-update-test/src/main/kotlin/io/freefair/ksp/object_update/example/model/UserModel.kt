package io.freefair.ksp.object_update.example.model

import io.freefair.updater.annotations.NotUpdatable
import io.freefair.updater.annotations.Updater
import java.time.LocalDateTime

@Updater
data class UserModel(
    var name: String,
    var age: Int,
    var email: String,
    var enabled: Boolean,
    var roles: List<String>,
    @NotUpdatable
    var created_at: LocalDateTime
)