package manager.ui.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.silent.core.data.Recipe
import com.silent.core.service.RecipesService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel<Recipe>(application) {

    override val service = RecipesService()


    fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = service.getAllData()
            when (request) {
                is ServiceResult.Error -> {
                    viewModelState.postValue(ViewModelBaseState.ErrorState(request.errorException))
                }
                is ServiceResult.Success -> {
                }
            }
        }
    }

    private fun getGroups(recipes: ArrayList<Recipe>) {

    }


}