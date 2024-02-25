package com.project.ecommercemvvmcz.ui.Home.Address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressData(
    val addressTitle:String,
    val fullname:String,
    val street:String,
    val phone:String,
    val city:String,
    val state:String,
):Parcelable
{
    constructor():this("","","","","","")
}

@Parcelize
data class AddressDatas(
    val fullAddress:String,
    val phone:String
):Parcelable
{
    constructor():this("","")
}
