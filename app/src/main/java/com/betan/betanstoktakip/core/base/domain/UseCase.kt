package com.betan.betanstoktakip.core.base.domain

import kotlinx.coroutines.flow.flow


abstract class UseCase<in PARAMS, RESULT> {
    @Throws
    abstract suspend fun run(params: PARAMS): RESULT

    open operator fun invoke(params: PARAMS) =
        flow<UseCaseState<RESULT>> {
            try {
                val result = run(params)
                emit(UseCaseState.Result(result))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(handleException(e))
            }
        }

    protected open fun handleException(e: Throwable): UseCaseState<RESULT> {
        e.printStackTrace()
        return UseCaseState.Fail(e.message.orEmpty())
    }
}
