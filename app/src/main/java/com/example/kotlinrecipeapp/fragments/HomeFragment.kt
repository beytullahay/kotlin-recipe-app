package com.example.kotlinrecipeapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kotlinrecipeapp.activites.MealActivity
import com.example.kotlinrecipeapp.adapter.MostPopularAdapter
import com.example.kotlinrecipeapp.databinding.FragmentHomeBinding
import com.example.kotlinrecipeapp.pojo.CategoryMeals
import com.example.kotlinrecipeapp.pojo.Meal
import com.example.kotlinrecipeapp.retrofit.MealApi
import com.example.kotlinrecipeapp.videoModel.HomeViewModel


class HomeFragment : Fragment() {

        private lateinit var binding: FragmentHomeBinding
        private lateinit var homeMvvm: HomeViewModel
        private lateinit var randomMeal: Meal
        private lateinit var popularItemsAdapter: MostPopularAdapter

        companion object{
            const val MEAL_ID = "com.example.kotlinrecipeapp.fragments.idMeal"
            const val MEAL_NAME = "com.example.kotlinrecipeapp.fragments.nameMeal"
            const val MEAL_THUMB = "com.example.kotlinrecipeapp.fragments.thumbMeal"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]

        popularItemsAdapter = MostPopularAdapter()

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


        preparePopularItemRecyclerView()

        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        homeMvvm.getPopularItems()
        observerPopularItemsLiveData()
        onPopularItemClick()



    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            meal ->
            val intent  = Intent (activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    // popular yana doğru recycler view
    private fun preparePopularItemRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false )
            adapter = popularItemsAdapter
        }
    }

    private fun observerPopularItemsLiveData() {
        homeMvvm.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList->
              popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<CategoryMeals>)
        }
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