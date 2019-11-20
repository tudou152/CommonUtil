package com.lliujun.utils.extensions


sealed class Condition<out T>

object Otherwise : Condition<Nothing>()
class TrueCondition<T>(val value: T) : Condition<T>()

inline fun <T> Boolean.yes(block: () -> T): Condition<T> {
    return when (this) {
        true -> TrueCondition(block())
        else -> Otherwise
    }
}

inline fun <T> Boolean.no(block: () -> T): Condition<T> {
    return when (!this) {
        true -> TrueCondition(block())
        else -> Otherwise
    }
}

inline fun <T> Condition<T>.otherwise(block: () -> T): T {
    return when (this) {
        Otherwise -> block()
        is TrueCondition -> this.value
    }
}