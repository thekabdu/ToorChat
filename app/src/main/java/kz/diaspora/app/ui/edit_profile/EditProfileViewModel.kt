package kz.diaspora.app.ui.edit_profile

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.MaritalStatusModel
import kz.diaspora.app.domain.model.User
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    val prefsImpl: PrefsImpl,
    private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }
    val userData: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    val statusData: MutableLiveData<List<MaritalStatusModel>> by lazy { MutableLiveData<List<MaritalStatusModel>>() }
    val token: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        getStatuses()
        getUserData()
    }

    fun getUserData() {
        launchIO {
            isRefreshing.postValue(true)

            userData.postValue(prefsImpl.getUser())
            token.postValue(prefsImpl.getToken())

            isRefreshing.postValue(false)
        }
    }

    fun getStatuses() {
        launchIO {
            isRefreshing.postValue(true)

            val data = baseCloudRepository.getStatuses()
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    statusData.postValue(data.value)
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun saveUserData(user: User) {
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.editUser(user)
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    prefsImpl.setUser(data.value)
                }
            }

            isRefreshing.postValue(false)
        }
    }

    fun onBookmarkClick(user: User) {
        userData.postValue(user)
    }
}