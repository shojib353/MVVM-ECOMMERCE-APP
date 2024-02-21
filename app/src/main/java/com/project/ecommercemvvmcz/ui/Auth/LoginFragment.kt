package com.project.ecommercemvvmcz.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.project.ecommercemvvmcz.MainActivity
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.FragmentLoginBinding
import com.project.ecommercemvvmcz.util.UiState
import com.project.ecommercemvvmcz.util.hide
import com.project.ecommercemvvmcz.util.isValidEmail
import com.project.ecommercemvvmcz.util.show
import com.project.ecommercemvvmcz.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {


    val TAG: String = "RegisterFragment"
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        observer()
        binding.loginBtn.setOnClickListener {
            if (validation()) {
                viewModel.login(
                    email = binding.emailEt.text.toString(),
                    password = binding.passEt.text.toString()
                )
            }
        }

        binding.forgotPassLabel.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.registerLabel.setOnClickListener {
           findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    fun observer(){



        viewModel.login.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.loginBtn.setText("")
                    binding.loginProgress.show()
                }
                is UiState.Failure -> {
                    binding.loginBtn.setText("Login")
                    binding.loginProgress.hide()
                    toast(state.message)
                }
                is UiState.Success -> {
                    binding.loginBtn.setText("Login")
                    binding.loginProgress.hide()
                    toast(state.data)
                    Intent(requireActivity(),MainActivity::class.java).also {intent ->
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        requireActivity().finish()
                    }

                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.emailEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter your email")
        }else{
            if (!binding.emailEt.text.toString().isValidEmail()){
                isValid = false
                toast("enter valid email")
            }
        }
        if (binding.passEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter password")
        }else{
            if (binding.passEt.text.toString().length < 8){
                isValid = false
                toast("enter valid pass")
            }
        }
        return isValid
    }





}