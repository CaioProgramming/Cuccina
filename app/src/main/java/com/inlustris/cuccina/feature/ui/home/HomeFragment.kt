package com.inlustris.cuccina.feature.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inlustris.cuccina.databinding.FragmentHomeBinding
import com.silent.core.data.Category
import com.silent.core.data.Recipe
import com.silent.core.ui.RecipeAdapter
import com.silent.core.ui.RecipeGroupAdapter
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.showSnackBar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        homeViewModel.getCategories()
    }

    private fun FragmentHomeBinding.setupView() {
        title.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        homeViewModel.viewModelState.observeForever {
            when (it) {
                ViewModelBaseState.RequireAuth -> Toast.makeText(
                    requireContext(),
                    "Usuário desconectado",
                    Toast.LENGTH_SHORT
                ).show()
                ViewModelBaseState.DataDeletedState -> TODO()
                ViewModelBaseState.LoadingState -> TODO()
                is ViewModelBaseState.DataRetrievedState -> TODO()
                is ViewModelBaseState.DataListRetrievedState -> {
                    view?.showSnackBar("${it.dataList.size} receitas encontradas")
                    _binding?.recyclerviewRecipes?.adapter =
                        RecipeAdapter(it.dataList as ArrayList<Recipe>, ::selectRecipe)
                }
                is ViewModelBaseState.DataSavedState -> TODO()
                is ViewModelBaseState.DataUpdateState -> TODO()
                is ViewModelBaseState.FileUploadedState -> TODO()
                is ViewModelBaseState.ErrorState -> Toast.makeText(
                    requireContext(),
                    "Ocorreu um erro desconhecido",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        homeViewModel.homeState.observe(viewLifecycleOwner) {
            when (it) {
                is HomeViewModel.HomeState.CategoriesRetrieved -> {
                    homeViewModel.getHome()
                }
                is HomeViewModel.HomeState.HomeGroupsRetrieved -> {
                    _binding?.recyclerviewRecipes?.adapter =
                        RecipeGroupAdapter(it.groups, ::selectRecipe)
                }
            }
        }
    }

    private fun selectRecipe(recipe: Recipe) {
        view?.showSnackBar("${recipe.name} selecionado.")
    }

    private fun selectCategory(category: Category) {
        view?.showSnackBar("Categoria ${category.name} selecionada.")
    }

}