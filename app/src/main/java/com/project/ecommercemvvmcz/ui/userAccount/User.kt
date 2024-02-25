package com.project.ecommercemvvmcz.ui.userAccount

data class User(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    var imagePath: String = ""
){
    constructor(): this("","","","","")
}