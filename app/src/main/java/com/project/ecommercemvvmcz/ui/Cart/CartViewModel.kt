package com.project.ecommercemvvmcz.ui.Cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ecommercemvvmcz.ui.Home.Model.CartProduct
import com.project.ecommercemvvmcz.ui.Home.Model.Products
import com.project.ecommercemvvmcz.ui.Home.Model.FirebaseCommon
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val common: FirebaseCommon
) :ViewModel() {

    private val _cartProduct= MutableStateFlow<UiState<List<CartProduct>>>(UiState.Loading())
    val cartProducts=_cartProduct.asStateFlow()

    private var cartProductDocument= emptyList<DocumentSnapshot>()

    val productPrice=cartProducts.map {
        when(it){
                is UiState.Success->{calculate(it.data!!)}
            else -> {null}
        }


    }

    private fun calculate(data:List<CartProduct>):Int {
            return data.sumBy{
                it.products.price.toInt()*it.quantity
            }

    }

    init {
        getCartProduct()
    }

    private fun getCartProduct(){
        viewModelScope.launch{_cartProduct.emit(UiState.Loading())}
        firestore.collection("user").document(auth.uid!!).collection("Cart")
            .addSnapshotListener { value, error ->
                if (error!=null || value==null){
                    viewModelScope.launch {

                        _cartProduct.emit(UiState.Failure(error?.message.toString()))
                    }
                }else{
                    cartProductDocument=value.documents
                    val cartProduct=value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _cartProduct.emit(UiState.Success(cartProduct)) }
                }
            }

    }

    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){
        val indexedValue=cartProducts.value.data?.indexOf(cartProduct)
        if (indexedValue!=null&&indexedValue!=-1){
            val documentId=cartProductDocument[indexedValue].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE->{

                    increaseQuantity(documentId)

                }
                FirebaseCommon.QuantityChanging.DECREASE->{
                    if(cartProduct.quantity>0)
                    {decreaseQuantity(documentId)
                        }
                    else{deleteProduct(documentId)
                    }
                }
            }
        }

    }
    fun deleteProduct(id: String){
        firestore.collection("user").document(auth.uid!!).collection("Cart")
            .document(id).delete()
    }

    private fun decreaseQuantity(documentId: String) {
        common.decreaseQuantity(documentId){result,error->
            if(error!=null)
                viewModelScope.launch { _cartProduct.emit(UiState.Failure(error.message.toString())) }
        }
    }

    private fun increaseQuantity(documentId: String) {
        common.increaseQuantity(documentId){result,error->
            if(error!=null)
                viewModelScope.launch { _cartProduct.emit(UiState.Failure(error.message.toString())) }
        }
    }

}