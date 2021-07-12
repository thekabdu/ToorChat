package kz.diaspora.app.ui.sign_in

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.diaspora.app.core.BaseViewModel
import kz.diaspora.app.data.cloud.ResultWrapper
import kz.diaspora.app.data.cloud.repository.BaseCloudRepository
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.domain.model.UserWithToken
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
        private val prefsImpl: PrefsImpl,
        private val baseCloudRepository: BaseCloudRepository
) : BaseViewModel() {

    private val TAG = this::class.java.simpleName
    val isRefreshing: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val loginData: MutableLiveData<UserWithToken> by lazy { MutableLiveData<UserWithToken>() }
    val error: MutableLiveData<ResultWrapper.Error> by lazy { MutableLiveData<ResultWrapper.Error>() }

    init {

    }

    fun login(name: String, password: String) {
        launchIO {
            isRefreshing.postValue(true)
            val data = baseCloudRepository.login(
                    name,
                    password
            )
            when (data) {
                is ResultWrapper.Error -> error.postValue(data)
                is ResultWrapper.Success -> {
                    loginData.postValue(data.value)
                    prefsImpl.setToken(data.value.access_token)
                    prefsImpl.setUser(data.value.user)
                }
            }
            isRefreshing.postValue(false)
        }
    }
}