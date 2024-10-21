package com.betan.betanstoktakip.presentation.shoppingcart.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.extensions.inflate
import com.betan.betanstoktakip.core.extensions.toMoney
import com.betan.betanstoktakip.databinding.ItemCartProductBinding
import com.betan.betanstoktakip.domain.model.CartProductModel

class ItemCartProductAdapter : ListAdapter<CartProductModel, ItemCartProductAdapter.ItemViewHolder>(
    CartProductModelsDiffCallback()
) {

    var plusClickListener: (CartProductModel) -> Unit = { }
    var minusClickListener: (CartProductModel) -> Unit = { }
    var itemClickListener: (CartProductModel) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            plusClickListener,
            minusClickListener,
            itemClickListener
        )
    }


    class ItemViewHolder(
        private val binding: ItemCartProductBinding
    ) : ViewHolder(binding.root) {

        fun bind(
            item: CartProductModel,
            plusClickListener: (CartProductModel) -> Unit,
            minusClickListener: (CartProductModel) -> Unit,
            itemClickListener: (CartProductModel) -> Unit
        ) {
            with(binding) {
                textViewName.text = item.name
                editTextAmount.setText(item.amount.toString())
                textViewTotalPrice.text = item.totalPrice.toMoney()
                root.click {
                    itemClickListener.invoke(item)
                }
                buttonPlus.click {
                    plusClickListener.invoke(item)
                }
                buttonMinus.click {
                    minusClickListener.invoke(item)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val binding = parent.inflate(ItemCartProductBinding::inflate)
                return ItemViewHolder(binding)
            }
        }
    }
}