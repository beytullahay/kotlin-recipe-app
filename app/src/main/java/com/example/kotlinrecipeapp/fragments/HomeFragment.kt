package com.example.kotlinrecipeapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kotlinrecipeapp.activites.CategoryMealsActivity
import com.example.kotlinrecipeapp.activites.MainActivity
import com.example.kotlinrecipeapp.activites.MealActivity
import com.example.kotlinrecipeapp.adapter.CategoriesAdapter
import com.example.kotlinrecipeapp.adapter.MostPopularAdapter
import com.example.kotlinrecipeapp.databinding.FragmentHomeBinding
import com.example.kotlinrecipeapp.pojo.MealsByCategory
import com.example.kotlinrecipeapp.pojo.Meal
import com.example.kotlinrecipeapp.videoModel.HomeViewModel


class HomeFragment : Fragment() {

        private lateinit var binding: FragmentHomeBinding
        private lateinit var viewModel: HomeViewModel
        private lateinit var randomMeal: Meal
        private lateinit var popularItemsAdapter: MostPopularAdapter
        private lateinit var categoriesAdapter: CategoriesAdapter

        companion object{
            const val MEAL_ID = "com.example.kotlinrecipeapp.fragments.idMeal"
            const val MEAL_NAME = "com.example.kotlinrecipeapp.fragments.nameMeal"
            const val MEAL_THUMB = "com.example.kotlinrecipeapp.fragments.thumbMeal"
            const val CATEGORY_NAME = "com.example.kotlinrecipeapp.fragments.categoryName"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
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

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()





    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemCick = {
            category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3 ,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }


    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            meal ->
            val intent  = Intent (activity, MealActivity ::class.java)
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



    private fun onRandomMealClick() {
       binding.randomMealCard.setOnClickListener{
           val intent = Intent(activity, MealActivity::class.java)
           intent.putExtra(MEAL_ID,randomMeal.idMeal)
           intent.putExtra(MEAL_NAME,randomMeal.strMeal)
           intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
           startActivity(intent)
       }
    }

    private fun observerPopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { mealList->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner) { t: Meal? ->
            // Glide ile resmi ImageView'e yazdırıyoruz.
            Glide.with(this@HomeFragment)
                .load(t?.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = t!!

        }
    }


    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer{
                categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }







}