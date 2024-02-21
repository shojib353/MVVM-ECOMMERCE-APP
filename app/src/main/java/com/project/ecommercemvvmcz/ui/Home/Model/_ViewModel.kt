package com.project.ecommercemvvmcz.ui.Home.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class _ViewModel @Inject constructor(
    private val firestore:FirebaseFirestore
):ViewModel() {


    private val _categoryProduct= MutableStateFlow<UiState<List<Products>>>(UiState.Loading())
     val categoryProduct:StateFlow<UiState<List<Products>>> = _categoryProduct

    private val _bestDealsProduct= MutableStateFlow<UiState<List<Products>>>(UiState.Loading())
    val bestDealsProduct:StateFlow<UiState<List<Products>>> = _bestDealsProduct

    private val _bestProduct= MutableStateFlow<UiState<List<Products>>>(UiState.Loading())
    val bestProduct:StateFlow<UiState<List<Products>>> = _bestProduct

    init {
        fetchCategoryProduct()
        fetchBestDealsProduct()
        fetchProduct()
    }

    private fun fetchProduct() {

        viewModelScope.launch {
            _bestProduct.emit(UiState.Loading())
        }
        firestore.collection("Products").get()
            .addOnSuccessListener {
                val bestProductList=it.toObjects(Products::class.java)
                viewModelScope.launch {
                    _bestProduct.emit(UiState.Success(bestProductList))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProduct.emit(UiState.Failure(it.message.toString()))
                }


            }

    }

    private fun fetchBestDealsProduct() {
        viewModelScope.launch {
            _bestDealsProduct.emit(UiState.Loading())
        }
        firestore.collection("Products").get()
            .addOnSuccessListener {
            val bestDealsProductList=it.toObjects(Products::class.java)
            viewModelScope.launch {
                _bestDealsProduct.emit(UiState.Success(bestDealsProductList))
            }

        }.addOnFailureListener {
            viewModelScope.launch {
                _bestDealsProduct.emit(UiState.Failure(it.message.toString()))
            }


    }
    }

    fun fetchCategoryProduct(){
        viewModelScope.launch {
            _categoryProduct.emit(UiState.Loading())
        }
        firestore.collection("Products").whereEqualTo("category","apple").get()
            .addOnSuccessListener {
                val categoryProductList=it.toObjects(Products::class.java)
                viewModelScope.launch {
                    _categoryProduct.emit(UiState.Success(categoryProductList))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _categoryProduct.emit(UiState.Failure(it.message.toString()))
                }
            }
    }
}