package com.yusuf.paparafinalcase.presentation.favoriteFoodScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.paparafinalcase.core.rootResult.RootResult
import com.yusuf.paparafinalcase.data.local.dao.FoodDao
import com.yusuf.paparafinalcase.data.local.model.LocalFoods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteFoodViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {

    private val _favoriteFoodsState = MutableStateFlow(FavoriteFoodState())
    val favoriteFoodsState: Flow<FavoriteFoodState> = _favoriteFoodsState

    init {
        fetchFavoriteFoods()
    }

    private fun fetchFavoriteFoods() {
        viewModelScope.launch {
            _favoriteFoodsState.update { it.copy(isLoading = true, error = null) }
            try {
                foodDao.getFoods().collect { favoriteFoods ->
                    _favoriteFoodsState.update {
                        it.copy(isLoading = false, favoriteFoods = favoriteFoods)
                    }
                }
            } catch (e: Exception) {
                _favoriteFoodsState.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }

    fun deleteFavoriteFood(foodId: Int) {
        viewModelScope.launch {
            try {
                foodDao.deleteFood(foodId)
                fetchFavoriteFoods()
            } catch (e: Exception) {
                _favoriteFoodsState.update {
                    it.copy(error = e.message)
                }
            }
        }
    }
}