package com.inlustris.cuccina.feature.profile.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ilustris.cuccina.feature.profile.domain.service.UserService
import com.inlustris.cuccina.feature.profile.domain.model.UserModel
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (application: Application,
                                             override val service: UserService) : BaseViewModel<UserModel>(application) {

    enum class UserState {
        LOGGED, NOT_LOGGED
    }

    val user = MutableLiveData<UserModel>()
    val userInfo = MutableLiveData<FirebaseUser>()
    val userState = MutableLiveData<UserState>()

    fun getUserData(userId: String? = null) {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            service.currentUser()?.let {
                val uid = if (userId.isNullOrEmpty()) it.uid else userId
                when (val userTask = service.getSingleData(uid)) {
                    is ServiceResult.Success -> {
                        delay(1000)
                        user.postValue(userTask.data as UserModel)
                        userInfo.postValue(it)
                    }
                    is ServiceResult.Error -> {
                        updateViewState(ViewModelBaseState.ErrorState(userTask.errorException))
                    }
                }
                updateViewState(ViewModelBaseState.LoadCompleteState)
            }
        }
    }

    fun disconnect() {
        FirebaseAuth.getInstance().signOut()
        userState.value = UserState.NOT_LOGGED
    }

    fun removeUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            service.deleteData(service.currentUser()!!.uid)
            getUser()?.run {
                val deleteTask = delete()
                if(deleteTask.isSuccessful) {
                    userState.value = UserState.NOT_LOGGED
                } else {
                    userState.value = UserState.LOGGED
                }
            }
        }
    }

}