package com.project.ecommercemvvmcz.ui.Home.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore:FirebaseFirestore,
    private val auth:FirebaseAuth,
    private val common: FirebaseCommon

):ViewModel() {

    private val _addToCart=MutableStateFlow<UiState<CartProduct>>(UiState.Loading())
    val addToCart=_addToCart.asStateFlow()

    private val __addToCart=MutableStateFlow<UiState<CartProduct>>(UiState.Loading())
    val aaddToCart=__addToCart.asStateFlow()

    fun isExit(cartProduct: CartProduct){
        firestore.collection("user").document(auth.uid!!).collection("Cart")
            .whereEqualTo("products.id",cartProduct.products.id).get().addOnSuccessListener {

                it.documents.let {
                    if(it.isEmpty()){
                        viewModelScope.launch { __addToCart.emit(UiState.Loading()) }





                    }
                    else{  viewModelScope.launch { __addToCart.emit(UiState.Success(cartProduct)) }}

                }
            }.addOnFailureListener {
                viewModelScope.launch { __addToCart.emit(UiState.Failure(it.message.toString())) }

            }


    }

    fun addUpdateProductInCart(cartProduct: CartProduct){
        viewModelScope.launch { _addToCart.emit(UiState.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("Cart")
            .whereEqualTo("products.id",cartProduct.products.id).get()
            .addOnSuccessListener { it ->

                it.documents.let {
                    if(it.isEmpty()){ //add
                        addNewProduct(cartProduct)

                    }else{
                        val x= mutableListOf<CartProduct>()

                        it.forEach {
                           val data= it.toObject(CartProduct::class.java)
                            x.add(data!!)

                        }
                        var productID = x.filter { s -> s.products.id == cartProduct.products.id }.single()
                       // val product=it.first().toObject(CartProduct::class.java)

                        if (productID!=null){//increase
                            val documentId=productID.products.id
                            increaseQuantity(documentId,cartProduct)

                        }else{//add

                            addNewProduct(cartProduct)

                        }
                    }

                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(UiState.Failure(it.message.toString())) }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct){
        common.addProductToCart(cartProduct){addedProduct,e->
            viewModelScope.launch {
                if (e==null){
                    _addToCart.emit(UiState.Success(addedProduct!!))
                }else{_addToCart.emit(UiState.Failure(e.message.toString()))}
            }

        }

    }
    private fun increaseQuantity(documentId:String,cartProduct: CartProduct){
        common.increaseQuantity(documentId){_,e->
            viewModelScope.launch {
                if (e==null)
                    _addToCart.emit(UiState.Success(cartProduct))
                else
                    _addToCart.emit(UiState.Failure(e.message.toString()))
            }

        }

    }

}