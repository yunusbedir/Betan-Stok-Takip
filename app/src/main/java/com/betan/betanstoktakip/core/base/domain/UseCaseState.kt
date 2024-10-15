package com.betan.betanstoktakip.core.base.domain
sealed class UseCaseState<T> {
    data class Result<T>(
        val data: T,
    ) : UseCaseState<T>()

    data class Fail<T>(
        val failure: String,
    ) : UseCaseState<T>()

    fun isFail(): Boolean = this is Fail

    fun getDataOrNull(): T? = (this as? Result)?.data
}
