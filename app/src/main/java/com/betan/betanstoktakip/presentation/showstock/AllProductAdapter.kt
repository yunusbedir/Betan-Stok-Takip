package com.betan.betanstoktakip.presentation.showstock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betan.betanstoktakip.databinding.ItemAllProductBinding
import com.betan.betanstoktakip.domain.model.ProductModel

class AllProductAdapter(
    private val productList: List<ProductModel>
) : RecyclerView.Adapter<AllProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemAllProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductModel) {
            binding.apply {
                textViewName.text = product.brandName
                textViewDescription.text = product.name
                textViewStockCount.text = "${product.stockAmount} Adet"
                textViewOneAmountPrice.text = "${product.purchasePrice} TL"
                textViewSalePrice.text = "${product.salePrice} TL"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemAllProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}
