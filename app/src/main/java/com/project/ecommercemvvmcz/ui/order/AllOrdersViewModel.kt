package com.project.ecommercemvvmcz.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _allOrders = MutableStateFlow<UiState<List<Order>>>(UiState.Loading())
    val allOrders = _allOrders.asStateFlow()

    init {
        getAllOrders()
    }

    fun getAllOrders(){
        viewModelScope.launch {
            _allOrders.emit(UiState.Loading())
        }

       // firestore.collection("user").document(auth.uid!!).collection("orders").
        firestore.collection("orders").whereEqualTo("orderAuthID",auth.uid)
            //.orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                orders.sortByDescending { it.date }


                viewModelScope.launch {
                    _allOrders.emit(UiState.Success(orders))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _allOrders.emit(UiState.Failure(it.message.toString()))
                }
            }
    }

}