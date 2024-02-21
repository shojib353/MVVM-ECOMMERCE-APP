package com.project.ecommercemvvmcz.ui.Home.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.ecommercemvvmcz.databinding.CartProductItemBinding
import com.project.ecommercemvvmcz.ui.Home.Model.CartProduct
import com.project.ecommercemvvmcz.ui.Home.Model.Products

class CartItemAdaptar  : RecyclerView.Adapter<CartItemAdaptar.CartViewHolder>() {
    inner class CartViewHolder( val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(CartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(CartProduct.products.images[0]).into(imageCartProduct)
                name.text=CartProduct.products.name
                price.text = "${CartProduct.products.price}"
                count.text=CartProduct.quantity.toString()


            }
        }
        }


    private val deffcallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.products.id == newItem.products.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, deffcallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false

            )
        )
    }



    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartP = differ.currentList[position]
        holder.bind(cartP)
        holder.binding.imgPlus.setOnClickListener {
            onPlusClick?.invoke(cartP)


        }
        holder.binding.cartDelete.setOnClickListener {
            onDeleteClick?.invoke(cartP)

        }
        holder.binding.imgMinus.setOnClickListener {
            onMinasClick?.invoke(cartP)

        }
    }

    override fun getItemCount(): Int {
            return differ.currentList.size
        }



    var onDeleteClick:((CartProduct)->Unit)?=null
    var onPlusClick:((CartProduct)->Unit)?=null
    var onMinasClick:((CartProduct)->Unit)?=null
}




