package com.project.ecommercemvvmcz

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.project.ecommercemvvmcz.databinding.ActivityMainBinding
import com.project.ecommercemvvmcz.ui.Auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()


    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragmentContainerView).navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navHostFragment.navController






        appBarConfig = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfig)
        binding.bottomNav.setupWithNavController(navController)



        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment2 -> findNavController(R.id.fragmentContainerView).navigate(R.id.homeFragment2)
                R.id.cartFragment2 -> findNavController(R.id.fragmentContainerView).navigate(R.id.cartFragment2)
                R.id.profileFragment2 -> findNavController(R.id.fragmentContainerView).navigate(R.id.profileFragment2)

            }
            true
        }

        navController.addOnDestinationChangedListener{_,d,_ ->

            when(d.id){
                R.id.homeFragment2->{
                    binding.toolbar.visibility=View.GONE
                    binding.bottomNav.visibility=View.VISIBLE
                }
                R.id.search,R.id.productDetails,R.id.forgotPasswordFragment2->{binding.bottomNav.visibility=View.GONE
                                binding.toolbar.visibility=View.GONE}
                R.id.billing,R.id.orderDetailFragment,R.id.allOrdersFragment->
                    {binding.bottomNav.visibility=View.GONE
                    binding.toolbar.visibility=View.VISIBLE

                }


                else ->{ binding.bottomNav.visibility=View.VISIBLE
                    binding.toolbar.visibility=View.VISIBLE}
            }


        }


        setContentView(binding.root)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if(navController.currentDestination?.id==R.id.homeFragment2){
            binding.bottomNav.visibility=View.VISIBLE
            super.onBackPressed()

        }
        else{
            super.onBackPressed()
        }
    }


}
