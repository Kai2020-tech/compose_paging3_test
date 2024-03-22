package com.example.compose_paging3_test.data

data class ApiResult<T>(val code: Int?, val message: String?, val data: T?, val errors: Map<String, List<String>>? = null)