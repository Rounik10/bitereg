package com.project.bitereg.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    var details: UserDetails? = null
)