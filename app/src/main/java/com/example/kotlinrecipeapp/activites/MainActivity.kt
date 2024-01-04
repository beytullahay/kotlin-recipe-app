package com.example.kotlinrecipeapp.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.kotlinrecipeapp.R
import com.example.kotlinrecipeapp.db.MealDatabase
import com.example.kotlinrecipeapp.videoModel.HomeViewModel
import com.example.kotlinrecipeapp.videoModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel : HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation,navController)

    }
}