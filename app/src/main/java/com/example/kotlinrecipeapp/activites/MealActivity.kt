package com.example.kotlinrecipeapp.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kotlinrecipeapp.R
import com.example.kotlinrecipeapp.databinding.ActivityMainBinding
import com.example.kotlinrecipeapp.databinding.ActivityMealBinding
import com.example.kotlinrecipeapp.databinding.FragmentHomeBinding
import com.example.kotlinrecipeapp.db.MealDatabase
import com.example.kotlinrecipeapp.fragments.HomeFragment
import com.example.kotlinrecipeapp.pojo.Meal
import com.example.kotlinrecipeapp.videoModel.HomeViewModel
import com.example.kotlinrecipeapp.videoModel.MealViewModel
import com.example.kotlinrecipeapp.videoModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var mealMvvm: MealViewModel
    private lateinit var youtubeLink: String
    private lateinit var binding: ActivityMealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageClick()
        onFavoriteClick()

    }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener(){
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal save", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal?=null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal>{

            override fun onChanged(t: Meal) {
                onResponseCase ()
                val meal = t
                mealToSave= meal

                binding.tvCategory.text = "Category : ${meal!!.strCategory}"
                binding.tvArea.text = "Area: ${meal!!.strArea}"
                binding.tvContent.text = meal.strInstructions

                youtubeLink = meal.strYoutube!!
            }


        })
    }



    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white)) // yukarı kaydırınca yazı beyaz olacak
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
    }


    private fun getMealInformationFromIntent(){
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!


    }


    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase (){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE

    }
}