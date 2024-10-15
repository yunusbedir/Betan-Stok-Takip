package com.betan.betanstoktakip.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betan.betanstoktakip.core.base.domain.UseCase
import com.betan.betanstoktakip.core.base.domain.UseCaseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _progressState = MutableStateFlow(false)
    val progressState: StateFlow<Boolean>
        get() = _progressState

    protected val _failState = Channel<String>()
    val failState: Flow<String>
        get() = _failState.receiveAsFlow()

    protected fun showProgress() {
        if (_progressState.value.not()) {
            _progressState.value = true
        }
    }

    protected fun hideProgress() {
        if (_progressState.value) {
            _progressState.value = false
        }
    }

    protected fun <T> MutableLiveData<T>.update(function: (T?) -> T?) {
        postValue(function(value))
    }

    protected fun <PARAMS, RESULT> UseCase<PARAMS, RESULT>.action(
        params: PARAMS,
        progress: Boolean = true,
        result: (RESULT) -> Unit,
    ) {

        viewModelScope.launch {
            if (progress) {
                showProgress()
            }
            invoke(params).collectLatest { useCaseState ->
                if (progress) {
                    hideProgress()
                }
                when (useCaseState) {
                    is UseCaseState.Fail -> {
                        _failState.trySend(useCaseState.failure)
                    }

                    is UseCaseState.Result -> {
                        result.invoke(useCaseState.data)
                    }
                }
            }
        }
    }

    protected fun <PARAMS, RESULT> UseCase<PARAMS, RESULT>.actionWithUseCaseState(
        params: PARAMS,
        progress: Boolean = true,
        result: (UseCaseState<RESULT>) -> Unit,
    ) {
        viewModelScope.launch {
            if (progress) {
                showProgress()
            }
            invoke(params).collectLatest { useCaseState ->
                if (progress) {
                    hideProgress()
                }
                result.invoke(useCaseState)
            }
        }
    }

    protected fun <PARAMS, RESULT> UseCase<PARAMS, RESULT>.actionWithoutFail(
        params: PARAMS,
        progress: Boolean = true,
        result: (RESULT) -> Unit,
    ) {
        viewModelScope.launch {
            if (progress) {
                showProgress()
            }
            invoke(params).collectLatest { useCaseState ->
                if (progress) {
                    hideProgress()
                }
                if (useCaseState is UseCaseState.Result) {
                    result.invoke(useCaseState.data)
                }
            }
        }
    }
}
