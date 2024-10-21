package com.betan.betanstoktakip.presentation.shoppingcart

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.core.extensions.toMoney
import com.betan.betanstoktakip.databinding.FragmentShoppingCartBinding
import com.betan.betanstoktakip.presentation.shoppingcart.adapter.ItemCartProductAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCartFragment : BaseFragment<FragmentShoppingCartBinding>(
    bindingInflater = FragmentShoppingCartBinding::inflate
) {

    private val viewModel: ShoppingCartViewModel by viewModels()

    private val itemCartProductAdapter = ItemCartProductAdapter()

    override fun setupViews(savedInstanceState: Bundle?) {
        setupListeners()
        binding.recyclerViewCartItems.adapter = itemCartProductAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.invoke(ShoppingCartContract.Action.GetCartProducts)
    }

    private fun setupListeners() {
        itemCartProductAdapter.plusClickListener = { item ->
            viewModel.invoke(ShoppingCartContract.Action.PlusCartProduct(item))
        }
        itemCartProductAdapter.minusClickListener = { item ->
            viewModel.invoke(ShoppingCartContract.Action.MinusCartProduct(item))
        }
        itemCartProductAdapter.itemClickListener = { item ->

        }
        with(binding) {
            buttonApplyDiscount.click {
                val discountCode = editTextDiscountCode.text.orEmpty()
                viewModel.invoke(
                    ShoppingCartContract.Action.DiscountCode(
                        code = discountCode
                    )
                )
            }
            buttonSell.click {

            }
        }
    }

    override fun collectData() {
        collect(viewModel.failState, ::collectFailState)
        collect(viewModel.uiState, ::collectUiState)
        collect(viewModel.eventState, ::collectEventState)
    }

    private fun collectEventState(event: ShoppingCartContract.Event) {
        when (event) {
            is ShoppingCartContract.Event.RemoveCartProduct -> {
                context?.let { safeContext ->
                    MaterialAlertDialogBuilder(safeContext)
                        .setIcon(R.drawable.ic_delete)
                        .setTitle("Uyarı")
                        .setMessage("Ürün sepetten silinecektir. Onaylıyor musun?")
                        .setPositiveButton("Onaylıyorum") { dialog, _ ->
                            viewModel.invoke(
                                ShoppingCartContract.Action.RemoveCartProduct(
                                    barcode = event.barcode
                                )
                            )
                            dialog.dismiss()
                        }.setNegativeButton("Hayır") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }
    }

    private fun collectUiState(uiState: ShoppingCartContract.UiState) {
        itemCartProductAdapter.submitList(uiState.cartProductModels)
        with(binding) {
            editTextDiscountCode.setText(uiState.discountCode)
            textViewTotalPrice.text = uiState.totalPrice.toMoney()
            textViewPaidPrice.text = uiState.discountedTotalPrice.toMoney()
            textViewTotalPrice.isVisible = uiState.totalPrice != uiState.discountedTotalPrice
        }
    }

}