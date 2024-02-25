package com.project.ecommercemvvmcz.ui.order

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
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel() {

    private val _order= MutableStateFlow<UiState<Order>>(UiState.Loading())
    val order= _order.asStateFlow()

    fun placeOrder(order:Order){
        viewModelScope.launch { _order.emit(UiState.Loading()) }
        firestore.runBatch {
            firestore.collection("user").document(auth.uid!!)
                .collection("orders")
                .document()
                .set(order)

            firestore.collection("orders").document().set(order)

            firestore.collection("user").document(auth.uid!!).collection("Cart")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch { _order.emit(UiState.Success(order)) }
        }.addOnFailureListener {
            viewModelScope.launch { _order.emit(UiState.Failure(it.message)) }
        }

    }
}