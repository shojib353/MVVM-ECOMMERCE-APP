package com.project.ecommercemvvmcz.ui.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.project.ecommercemvvmcz.data.model.User
import com.project.ecommercemvvmcz.databinding.FragmentRegisterBinding
import com.project.ecommercemvvmcz.util.UiState
import com.project.ecommercemvvmcz.util.hide
import com.project.ecommercemvvmcz.util.isValidEmail
import com.project.ecommercemvvmcz.util.show
import com.project.ecommercemvvmcz.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    val TAG: String = "RegisterFragment"
    lateinit var binding: FragmentRegisterBinding
    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.registerBtn.setOnClickListener {
            if (validation()){
                viewModel.register(
                    email = binding.emailEt.text.toString(),
                    password = binding.passEt.text.toString(),
                    user = getUserObj()
                )
            }
        }
    }

    fun observer() {
        viewModel.register.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.registerBtn.setText("")
                    binding.registerProgress.show()
                }
                is UiState.Failure -> {
                    binding.registerBtn.setText("Register")
                    binding.registerProgress.hide()
                    toast(state.message)
                }
                is UiState.Success -> {
                    binding.registerBtn.setText("Register")
                    binding.registerProgress.hide()
                    toast(state.data)
                    //mainActivity.restart()

                }
            }
        }
    }

    fun getUserObj(): User {
        return User(
            id = "",
            first_name = binding.firstNameEt.text.toString(),
            last_name = binding.lastNameEt.text.toString(),
            phone = binding.jobTitleEt.text.toString(),
            email = binding.emailEt.text.toString(),
        )
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.firstNameEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter_first_name")
        }

        if (binding.lastNameEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter_last_name")
        }

        if (binding.jobTitleEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter_phone_number")
        }

        if (binding.emailEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter_email")
        }else{
            if (!binding.emailEt.text.toString().isValidEmail()){
                isValid = false
                toast("invalid_email")
            }
        }
        if (binding.passEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter_password")
        }else{
            if (binding.passEt.text.toString().length < 8){
                isValid = false
                toast("invalid_password")
            }
        }
        return isValid
    }

}