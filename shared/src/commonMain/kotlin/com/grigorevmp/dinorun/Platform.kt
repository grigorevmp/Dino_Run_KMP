package com.grigorevmp.dinorun

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform