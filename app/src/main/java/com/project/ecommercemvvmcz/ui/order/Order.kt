package com.project.ecommercemvvmcz.ui.order

import android.os.Parcelable
import com.project.ecommercemvvmcz.ui.Home.Address.AddressData
import com.project.ecommercemvvmcz.ui.Home.Address.AddressDatas
import com.project.ecommercemvvmcz.ui.Home.Model.CartProduct
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderAuthID: String = "",
    val orderStatus: String = "",
    val totalPrice: Int = 0,
    val products: List<CartProduct> = emptyList(),
    val address: AddressDatas = AddressDatas(),
    val date: String = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH).format(Date()),
    val orderId: Long = nextLong(0,100_000_000_000) + totalPrice.toLong()
):Parcelable