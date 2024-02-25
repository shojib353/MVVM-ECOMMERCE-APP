package com.project.ecommercemvvmcz.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.project.ecommercemvvmcz.MainActivity
import com.project.ecommercemvvmcz.MainActivity2
import com.project.ecommercemvvmcz.R
import com.project.ecommercemvvmcz.databinding.FragmentProfileBinding
import com.project.ecommercemvvmcz.ui.Auth.AuthViewModel
import com.project.ecommercemvvmcz.ui.Home.Model.ProfileViewModel
import com.project.ecommercemvvmcz.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    val viewModel by viewModels<ProfileViewModel>()
    val authViewModel by viewModels<AuthViewModel>()
    //: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentProfileBinding.inflate(layoutInflater)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // binding.tvUserName.text = "blk ${viewModel.user.value.data!!.firstName} "
        lifecycleScope.launchWhenStarted {
            viewModel.user.collect {
                when (it) {
                    is UiState.Loading -> {
                        binding.progressbarSettings.visibility = View.VISIBLE

                    }
                    is UiState.Success -> {
                        binding.progressbarSettings.visibility = View.GONE
                        Glide.with(requireView()).load(it.data!!.imagePath).error(ColorDrawable(
                            Color.BLACK)).into(binding.imageUser)

                        binding.tvUserName.text = "${it.data!!.firstName} ${it.data.lastName}"
                       // Toast.makeText(requireContext(),"dd ${it.data!!.firstName} ${it.data.lastName}", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Failure -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarSettings.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }



        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_userAccountFragment)
        }
        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment2_to_allOrdersFragment)
        }
        binding.logout.setOnClickListener {
            authViewModel.logout {

                Intent(requireActivity(),MainActivity2::class.java).also {intent ->
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finish()

            }
        }


        //binding.tvVersion.text ="Version ${BuildConfig}"
}



}
}