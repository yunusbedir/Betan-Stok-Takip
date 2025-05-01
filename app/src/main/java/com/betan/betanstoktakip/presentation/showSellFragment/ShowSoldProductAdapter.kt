package com.betan.betanstoktakip.presentation.showSellFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betan.betanstoktakip.databinding.ItemSoldProductBinding
import com.betan.betanstoktakip.domain.model.SoldProductsModel

class SoldProductAdapter(private val items: List<SoldProductsModel.Item>) :
    RecyclerView.Adapter<SoldProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemSoldProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SoldProductsModel.Item) {
            binding.textViewProductName.text = item.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemSoldProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
