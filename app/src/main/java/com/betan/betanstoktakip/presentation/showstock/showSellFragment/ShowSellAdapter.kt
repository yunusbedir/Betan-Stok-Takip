package com.betan.betanstoktakip.presentation.showstock.showSellFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betan.betanstoktakip.databinding.ItemShowSellBinding
import com.betan.betanstoktakip.domain.model.ShowSellModel

class ShowSellAdapter(
private val saleslist: List<ShowSellModel>
) : RecyclerView.Adapter<ShowSellAdapter.ShowSellViewHolder>() {

    inner class ShowSellViewHolder(private val binding: ItemShowSellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(sales: ShowSellModel) {
            binding.apply {
                textViewTime.text="${sales.date}"
                textViewDescription.text = sales.description
                textViewDiscountCode.text = sales.discountCode
                textViewPaidPrice.text = "${sales.paidPrice}  TL"
                textViewOneAmountPrice.text = "${sales.totalPrice} TL"
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

    override fun getItemCount(): Int =saleslist.size

    override fun onBindViewHolder(holder: ShowSellViewHolder, position: Int) {
        holder.bind(saleslist[position])
    }
}