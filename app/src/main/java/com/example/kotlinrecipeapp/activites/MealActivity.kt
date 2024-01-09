package com.example.kotlinrecipeapp.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kotlinrecipeapp.R
import com.example.kotlinrecipeapp.databinding.ActivityMealBinding
import com.example.kotlinrecipeapp.db.MealDatabase
import com.example.kotlinrecipeapp.fragments.HomeFragment
import com.example.kotlinrecipeapp.pojo.Meal
import com.example.kotlinrecipeapp.viewModel.MealViewModel
import com.example.kotlinrecipeapp.viewModel.MealViewModelFactory

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

        // viewmodel bağlanıyor.
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId) // viewmodel'deki api çağrısı
        observerMealDetailsLiveData()

        onYoutubeImageClick()
        onFavoriteClick()

    }

    // yemeği favorilere ekleme. Local db'ye ekliyoruz burada.
    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener(){
            // null değil ise içindekini yap
            mealToSave?.let {
                mealMvvm.insertMeal(it) // db'ye ekle
                Toast.makeText(this,"Meal save", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // youtube iconuna tıklanılınca intent ile youtube linkiyle appi açıyor.
    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal?=null // null olabilen Meal modelinde
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

    // Yemek bilgilerinin alınması, daha sonra buradan alınan veriler ekrana yansıtılacak.
    private fun getMealInformationFromIntent(){
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!


    }

    // Yemek görseli ve isminin ekranda gösterilemesi
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        // başlık yazısını beyaza settik ve ismini yazdırdık
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white)) // yukarı kaydırınca yazı beyaz olacak

    }





    // verilere gelene kadar araçların görünürlüğünün ayarlanması.
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