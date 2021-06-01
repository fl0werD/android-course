package com.example.fl0wer.mapper

interface Mapper<From, To> {
    fun map(from: From): To
    fun map(from: Iterable<From>): List<To> = from.map { map(it) }
}
