package com.example.kotlinrecipeapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kotlinrecipeapp.activites.MealActivity
import com.example.kotlinrecipeapp.databinding.FragmentHomeBinding
import com.example.kotlinrecipeapp.pojo.Meal
import com.example.kotlinrecipeapp.retrofit.MealApi
import com.example.kotlinrecipeapp.videoModel.HomeViewModel


class HomeFragment : Fragment() {

        private lateinit var binding: FragmentHomeBinding
        private lateinit var homeMvvm: HomeViewModel
        private lateinit var randomMeal: Meal

        companion object{
            const val MEAL_ID = "com.example.kotlinrecipeapp.fragments.idMeal"
            const val MEAL_NAME = "com.example.kotlinrecipeapp.fragments.nameMeal"
            const val MEAL_THUMB = "com.example.kotlinrecipeapp.fragments.thumbMeal"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()



    }

    private fun onRandomMealClick() {
       binding.randomMealCard.setOnClickListener{
           val intent = Intent(activity, MealActivity::class.java)
           intent.putExtra(MEAL_ID,randomMeal.idMeal)
           intent.putExtra(MEAL_NAME,randomMeal.strMeal)
           intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
           startActivity(intent)
       }
    }

    private fun observerRandomMeal() {
        homeMvvm.randomMealLiveData.observe(viewLifecycleOwner) { t: Meal? ->
            // Glide ile resmi ImageView'e yazdırıyoruz.
            Glide.with(this@HomeFragment)
                .load(t?.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = t!!

        }
    }







}