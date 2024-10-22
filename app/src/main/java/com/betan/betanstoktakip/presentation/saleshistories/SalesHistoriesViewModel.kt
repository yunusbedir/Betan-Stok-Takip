package com.betan.betanstoktakip.presentation.saleshistories

import com.betan.betanstoktakip.core.base.BaseViewModel
import com.betan.betanstoktakip.domain.usecases.saleshistories.GetSalesHistoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SalesHistoriesViewModel @Inject constructor(
    private val getSalesHistoriesUseCase: GetSalesHistoriesUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SalesHistoriesContract.UiState())
    val uiState: StateFlow<SalesHistoriesContract.UiState>
        get() = _uiState

    fun invoke(action: SalesHistoriesContract.Action) {
        when (action) {
            SalesHistoriesContract.Action.GetSalesHistories -> getSalesHistories()
        }
    }

    private fun getSalesHistories() {
        getSalesHistoriesUseCase.action(Unit) { result ->
            _uiState.update { state ->
                state.copy(
                    items = result
                )
            }
        }
    }
}