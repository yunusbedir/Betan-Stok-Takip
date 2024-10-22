package com.betan.betanstoktakip.presentation.saleshistories

import com.betan.betanstoktakip.core.extensions.orZero
import com.betan.betanstoktakip.domain.model.SalesHistoriesModel

object SalesHistoriesContract {

    sealed interface Action {
        data object GetSalesHistories : Action
    }

    data class UiState(
        val items: List<SalesHistoriesModel> = listOf()
    ) {
        val totalPaidPrice: Double
            get() = items.sumOf { it.paidPrice.orZero() }

        val totalNormalPrice: Double
            get() = items.sumOf { it.totalPrice.orZero() }
    }
}