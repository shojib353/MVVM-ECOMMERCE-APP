package com.project.ecommercemvvmcz.ui.Billing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.project.ecommercemvvmcz.databinding.BillingProductsRvItemBinding
import com.project.ecommercemvvmcz.ui.Home.Model.CartProduct

class BillingProductAdapter:Adapter<BillingProductAdapter.BillingProductViewHolder>() {

    inner class BillingProductViewHolder
        (val binding:BillingProductsRvItemBinding):ViewHolder(binding.root)
    {
            fun bind(binlingProduct:CartProduct){
                binding.apply {
                    Glide.with(itemView).load(binlingProduct.products.images[0]).into(imageCartProduct)
                    tvProductCartName.text=binlingProduct.products.name
                    tvBillingProductQuantity.text=binlingProduct.quantity.toString()
                    tvProductCartPrice.text=binlingProduct.products.price.toString()
                }

            }

    }
    private val diffUtil= object :  DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.products==newItem.products
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem==newItem
        }


    }

    val differ=AsyncListDiffer(this,diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewHolder {
        return BillingProductViewHolder(
            BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val billingProduct=differ.currentList[position]
        holder.bind(billingProduct)
    }
}