package com.project.ecommercemvvmcz.ui.Home.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Products(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>
): Parcelable {
    constructor():this("0","","",0f,images= emptyList())
}
