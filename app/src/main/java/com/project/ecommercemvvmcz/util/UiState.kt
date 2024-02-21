package com.project.ecommercemvvmcz.util

sealed class UiState<T>(
    val data: T?=null,
    val message:String?=null
) {
    class Loading<T>: UiState<T>()
    class Success<T>(data: T): UiState<T>(data)
    class Failure<T>(message: String?): UiState<T>(message=message)
}