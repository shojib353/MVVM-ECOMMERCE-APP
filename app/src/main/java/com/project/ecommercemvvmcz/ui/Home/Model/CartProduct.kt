package com.project.ecommercemvvmcz.ui.Home.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartProduct(
    val products: Products,
    val quantity:Int

):Parcelable
{
    constructor():this(Products(),1)
}
