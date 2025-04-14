package com.betan.betanstoktakip.presentation.showstock.showSellFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betan.betanstoktakip.databinding.ItemShowSellBinding
import com.betan.betanstoktakip.domain.model.SoldProductsModel
import java.text.SimpleDateFormat
import java.util.Locale

class ShowSellAdapter(
    private val saleslist: List<SoldProductsModel>
) : RecyclerView.Adapter<ShowSellAdapter.ShowSellViewHolder>() {

    inner class ShowSellViewHolder(private val binding: ItemShowSellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(sales: SoldProductsModel) {
            binding.apply {
                // Satış tarihi
                val date = sales.date?.toDate()
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr")) // örn: 11 Nisan 2025
                val formattedDate = date?.let { dateFormat.format(it) } ?: "Tarih yok"
                textViewTime.text = formattedDate

                // İndirim kodu
                textViewDiscountCode.text = sales.discountCode

                // Ürün isimlerini alt alta yaz
                recyclerViewProducts.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = SoldProductAdapter(sales.items)
                }

                // Toplam alış fiyatı
                val totalPurchasePrice = sales.items.sumOf { it.paidPrice }

                // Fiyatlar
                textViewPaidPrice.text = "$totalPurchasePrice TL"
                textViewOneAmountPrice.text = " ${sales.totalPrice} TL"


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowSellViewHolder {
        val binding = ItemShowSellBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShowSellViewHolder(binding)

    }

    override fun getItemCount(): Int = saleslist.size

    override fun onBindViewHolder(holder: ShowSellViewHolder, position: Int) {
        holder.bind(saleslist[position])
    }
}
