package com.project.ecommercemvvmcz.ui.Home.Model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(
    private val Firestore: FirebaseFirestore,

private val auth: FirebaseAuth) {
    private val cartCollection= Firestore.collection("user")
        .document(auth.uid!!).collection("Cart")

    fun addProductToCart(cartProduct: CartProduct, onResult:(CartProduct?, Exception?)->Unit)
    {
        cartCollection.document(cartProduct.products.id).set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct,null)

            }.addOnFailureListener {
                onResult(null,it)
            }
    }
    fun increaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        Firestore.runTransaction {transition->
            val documentref=cartCollection.document(documentId)
            val document=transition.get(documentref)
            val productObject=document.toObject(CartProduct::class.java)
            productObject?.let {
                val newQuanty=it.quantity+1
                val newProductObject=it.copy(quantity = newQuanty)
                transition.set(documentref,newProductObject)

            }

        }.addOnSuccessListener {

            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }



    fun decreaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        Firestore.runTransaction {transition->
            val documentref=cartCollection.document(documentId)
            val document=transition.get(documentref)
            val productObject=document.toObject(CartProduct::class.java)
            productObject?.let {
                var newQuanty=it.quantity
                if (it.quantity<=1){
                    newQuanty=it.quantity*0+1

                }else{newQuanty=it.quantity-1}
                val newProductObject=it.copy(quantity = newQuanty)
                transition.set(documentref,newProductObject)

            }

        }.addOnSuccessListener {

            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }

    enum class QuantityChanging{
        INCREASE,DECREASE
    }
}