package com.project.ecommercemvvmcz.ui.Home.Adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.red
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ecommercemvvmcz.databinding.ProductItemBinding
import com.project.ecommercemvvmcz.ui.Home.Model.Products

class ProductAdapter:RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ProductItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(products: Products){
            binding.apply {
                Glide.with(itemView).load(products.images[0]).into(imgProduct)
                oldPrice.text="$ ${products.offerPercentage}"
                newPrice.text="$ ${products.price}"
                tvProductName.text=products.name
                oldPrice.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG.red

            }


        }
    }
    private val differCallback=object : DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id==newItem.id
        }

    }
    val differ= AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ProductItemBinding.
        inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onclick?.invoke(product)
        }
    }
    var onclick:((Products)-> Unit)?=null

}